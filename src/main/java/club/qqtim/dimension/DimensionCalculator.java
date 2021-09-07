package club.qqtim.dimension;

import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 异步执行试算
 *
 * 使用方式
 *  1. LEFT_EXPRESSION_FUNC_MAP 中初始化对应的维度接口
 *  2. 字典表rule-dict中右维度添加对应的维值
 *  3. OperatorType 定义想要添加的运算方式
 */
@Data
@Slf4j
@RequiredArgsConstructor
public class DimensionCalculator <IU>{


    /**
     * 考虑趋同，尽量都取正数，符号位不要了
     * 限制每个维度值范围只能63位，即最多有63个值
     */
    private static final int DIMENSION_LIMIT = 63;

    /**
     * 输入单元 输入最小单元，每个最小单元包含该单元的不同属性
     */
    private final List<IU> inputUnits;

    /**
     * 期望匹配的规则组，组按 priority 决定优先级，使用ruleGroupLimits 进行运算
     */
    private final List<RuleGroup> ruleGroupList;


    /**
     * 计算单元获取维值的方案
     */
    private final Map<String, Function<IU, Object>> expressionFuncMap;


    /**
     * 消费支持
     */
    private final Consumer<Map<Long, List<IU>>> consumer;






    /**
     * 运算表达式
     */
    @Getter
    private enum OperatorType  {

        /**
         * 暂时不允许出现这种运算符，出现代表表达式有问题，仅用来免除空指针报黄警告
         */
        UNDEFINED(0, "UNDEFINED", "未定义", (a, b) -> true),

        /**
         * a 包含于 b， 即 a 是 b 的子集
         */
        INCLUDE(1, "INCLUDE", "包含于", (a, b) ->  (a & b) == a),

        /**
         * a 完全不包含于 b， 即 a b 交集为空
         */
        NOT_INCLUDE(2, "NOT_INCLUDE", "不包含于", (a, b) ->  (a & b) == 0);

        private final Integer id;
        /** 权限限制类型代码 */
        private final String code;
        /** 限制类型描述类型名称*/
        private final String name;
        /** 运算表达式 **/
        private final BiPredicate<Long, Long> expected;

        OperatorType(Integer id, String code, String name, BiPredicate<Long, Long> expected) {
            this.id = id;
            this.code = code;
            this.name = name;
            this.expected = expected;
        }

        public static OperatorType parse(String code){
            for (OperatorType operatorType : OperatorType.values()) {
                if (operatorType.getCode().equals(code)) {
                    return operatorType;
                }
            }
            return UNDEFINED;
        }

    }




    public void calc() {
        // 建立位图映射
        Map<String, Map<Object, Long>> dimensionValBitMap = buildDimensionValMap();
        // 应用匹配规则进行试算
        versionRuleCalc(dimensionValBitMap);
    }





    /**
     * 根据全部输入单元和规则配置生成所选维度
     * key 是维度（编码） 1001    key 维度值    val 维度值对应的 bitmap
     * key dimension, val <key: dimensionVal, val: dimensionValBit>
     */
    private Map<String, Map<Object, Long>>  buildDimensionValMap() {
        // 将这些值的下标作为位图下标映射成一个64位数
        // key dimension, val <key: dimensionVal, val: dimensionValBit>
        Map<String, Map<Object, Long>> dimensionValBitMap = new HashMap<>(2);

        // 按所有已知维度遍历
        for (Map.Entry<String, Function<IU, Object>> leftExpressionFuncEntry : expressionFuncMap.entrySet()) {
            final String dimensionKey = leftExpressionFuncEntry.getKey();
            final Function<IU, Object> leftExpressionFunc = leftExpressionFuncEntry.getValue();

            final List<Object> allValues = Stream.of(
                            // 左表达式求值
                            inputUnits.stream().map(leftExpressionFunc).map(e ->
                                    // 返回值可能一个也可能多个
                                e instanceof Collection ? new ArrayList<>((Collection<?>) e) : Collections.singletonList(e)
                            ).flatMap(List::stream),
                            // 右表达式求值
                            ruleGroupList.stream().map(RuleGroup::getBindRuleList)
                                    // 找到所有组中所有条件为该左维度的
                                    .flatMap(List::stream).filter(e -> dimensionKey.equals(e.getLeftExpression()))
                                    // 取出对应的右维度值
                                    .map(Rule::getRightExpression).flatMap(List::stream))
                    .flatMap(Function.identity()).distinct() // 去重：左右的存在的维度值均可能出现自身重复或者交叉重复
                    .sorted().collect(Collectors.toList()); // 排序 为了满足值比较

            if (allValues.size() > DIMENSION_LIMIT) {
                throw new IllegalArgumentException("暂不支持单维度超过63个维值");
            }

            // key 维度值  val 维度值对应的位图
            Map<Object, Long> valBitMap = new HashMap<>(16);
            for (int i = 0; i < allValues.size(); i++) {
                valBitMap.put(allValues.get(i), 1L << i);
            }
            // 映射该维度对应所有维度值位图记录
            dimensionValBitMap.put(dimensionKey, valBitMap);
        }
        return dimensionValBitMap;
    }

    private Map<Long, List<IU>> versionRuleCalc(Map<String, Map<Object, Long>> dimensionValBitMap) {
        // 按优先级排序
        ruleGroupList.sort(Comparator.comparing(RuleGroup::getPriority));

        Map<Long, List<IU>> ruleAllocInputUnit = new HashMap<>(16);

        for (RuleGroup ruleGroup : ruleGroupList) {

            List<IU> currentRuleUnits = new ArrayList<>();

            final List<Rule<?>> ruleObject = ruleGroup.getBindRuleList();

            for (IU inputUnit : inputUnits) {

                boolean match = true;
                for (Rule<?> ruleContent : ruleObject) {
                    final String leftExpression = ruleContent.getLeftExpression();

                    final Map<Object, Long> currentDimensionValBitMap = dimensionValBitMap.get(leftExpression);

                    final Object leftDimensionVal = expressionFuncMap.get(leftExpression).apply(inputUnit);
                    Long leftDimensionValBit = currentDimensionValBitMap.get(leftDimensionVal);


                    // 同样的右边的表达式也对应一个数(由右边的所有选中的值或运算得来）todo: 如果可选全部，那么全为1
                    final Long rightDimensionValBit = ruleContent.getRightExpression().stream()
                            .map(currentDimensionValBitMap::get).reduce(0L, (a, b) -> a | b);


                    // 支持的运算有两种，包含于 即输入单元该维度值 & 运算后 等于原先的左维度维值 不包含与 则 & 运算后 等于 0
                    final BiPredicate<Long, Long> currentOperator =
                            OperatorType.parse(ruleContent.getOperateExpression()).getExpected();

                    if (!currentOperator.test(leftDimensionValBit, rightDimensionValBit)) {
                        match = false;
                    }
                }
                if (match) {
                    currentRuleUnits.add(inputUnit);
                }

            }
            // A 组搞完了，接着整 B 组
            // 如果当前组分群时找到的剩余单元为空，就不进行试算了
            if (CollectionUtils.isNotEmpty(currentRuleUnits)) {
                 //  todo 添加使用逻辑
                ruleAllocInputUnit.put(ruleGroup.getId(), currentRuleUnits);
                // 移除该组使用的，剩下的继续循环计算
                inputUnits.removeAll(currentRuleUnits);
            }
        }

        // 剩下的单元都进入 默认组
        if (CollectionUtils.isNotEmpty(inputUnits)) {
            //  todo 添加使用逻辑
            ruleAllocInputUnit.put(0L, inputUnits);
            // 移除该组使用的，剩下的继续循环计算
        }
        // 增强支持消费
        consumer.accept(ruleAllocInputUnit);
        return ruleAllocInputUnit;
    }


    public static void main(String[] args) throws Exception {
        // 输入单元
        List<InputUnit> inputUnits = new ArrayList<>();
        inputUnits.add(new InputUnit(1L, "Li", "001"));
        inputUnits.add(new InputUnit(2L, "Chu", "002"));
        inputUnits.add(new InputUnit(3L, "Yun", "003"));
        inputUnits.add(new InputUnit(4L, "Fei", "003"));
        inputUnits.add(new InputUnit(5L, "Long", "004"));

        // 计算规则组
        List<RuleGroup> ruleGroups = new ArrayList<>();
        final RuleGroup firstRuleGroup = new RuleGroup();
        firstRuleGroup.setId(1L);
        firstRuleGroup.setPriority(10);
        // 计算规则明细
        // 第一组 Id 不包含 2，3  名称包含 Li, haha
        {
            final Rule<Long> idNotIncludeRule = new Rule<>();
            idNotIncludeRule.setLeftExpression("ID");
            idNotIncludeRule.setOperateExpression(OperatorType.NOT_INCLUDE.getCode());
            idNotIncludeRule.setRightExpression(Arrays.asList(2L, 3L));
            final Rule<String> nameIncludeRule = new Rule<>();
            nameIncludeRule.setLeftExpression("NAME");
            nameIncludeRule.setOperateExpression(OperatorType.INCLUDE.getCode());
            nameIncludeRule.setRightExpression(Arrays.asList("Li", "Fei"));
            firstRuleGroup.setBindRuleList(Arrays.asList(idNotIncludeRule, nameIncludeRule));
            ruleGroups.add(firstRuleGroup);
        }


        {
            final RuleGroup secondRuleGroup = new RuleGroup();
            secondRuleGroup.setId(2L);
            secondRuleGroup.setPriority(11);
            // 计算规则明细
            final Rule<Long> idNotIncludeRule = new Rule<>();
            idNotIncludeRule.setLeftExpression("ID");
            idNotIncludeRule.setOperateExpression(OperatorType.INCLUDE.getCode());
            idNotIncludeRule.setRightExpression(Arrays.asList(2L, 3L));
            final Rule<String> nameIncludeRule = new Rule<>();
            nameIncludeRule.setLeftExpression("NAME");
            nameIncludeRule.setOperateExpression(OperatorType.NOT_INCLUDE.getCode());
            nameIncludeRule.setRightExpression(Arrays.asList("Fei", "haha"));
            secondRuleGroup.setBindRuleList(Arrays.asList(idNotIncludeRule, nameIncludeRule));
            ruleGroups.add(secondRuleGroup);
        }


        // 取维度值函数
        Map<String, Function<InputUnit, Object>> functionMap = new HashMap<>(2);
        functionMap.put("ID", InputUnit::getId);
        functionMap.put("NAME", InputUnit::getName);
//        functionMap.put("20003", inputUnit -> {
//            log.info("implement method");
//            return StringUtils.isNotEmpty(inputUnit.getName()) ? "1": "0";
//        });

        DimensionCalculator<InputUnit> calculator = new DimensionCalculator<>(
                inputUnits, ruleGroups, functionMap,

                map -> map.forEach((key, value) -> log.info("k {} v{} ", key, value))
        );

        calculator.calc();

    }


}
