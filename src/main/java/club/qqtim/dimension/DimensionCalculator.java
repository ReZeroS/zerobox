package club.qqtim.dimension;

import org.apache.commons.collections.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.stream.Collectors;

public class DimensionCalculator {


    /**
     * 考虑趋同，尽量都取正数，符号位不要了
     * 限制每个维度值范围只能63位，即最多有63个值
     */
    private static final int DIMENSION_LIMIT = 63;


    /**
     * 计算单元获取维值的方案
     */
    private static final Map<String, Function<InputUnit, String>> LEFT_EXPRESSION_FUNC_MAP = new HashMap<>(2);

    static {
        LEFT_EXPRESSION_FUNC_MAP.put("20001", InputUnit::getName);
        LEFT_EXPRESSION_FUNC_MAP.put("20002", InputUnit::getOtherExtend);

    }


    /**
     * 异步执行试算
     *
     * 暂时强约束：算法要求维度求值必须是 string
     *
     * 使用方式
     *  1. LEFT_EXPRESSION_FUNC_MAP 中初始化对应的维度接口
     *  2. 字典表rule-dict中右维度添加对应的维值
     *  3. OperatorType 定义想要添加的运算方式
     * @param inputUnits 输入最小单元，每个最小单元包含该单元的不同属性
     * @param versionRules 默认按 groupPriorityOrder 字段有序执行
     * @param defaultRule 剩余输入单元最后执行默认规则
     */
    private void doAllocInputUnits(List<InputUnit> inputUnits, List<VersionRule> versionRules, VersionRule defaultRule)  {
        Map<String, Map<String, Long>> dimensionValBitMap = buildDimensionValMap(inputUnits, versionRules);
        versionRuleCalc(inputUnits, versionRules, defaultRule, dimensionValBitMap);
        // 应用匹配规则进行试算
    }




    /**
     * 根据全部输入单元和规则配置生成所选维度
     * key 是维度（编码） 1001    key 维度值    val 维度值对应的 bitmap
     * key dimension, val <key: dimensionVal, val: dimensionValBit>
     */
    private Map<String, Map<String, Long>> buildDimensionValMap(List<InputUnit> inputUnits, List<VersionRule> versionRules) {
        // 获取所有维度对应的值 （来源于 左表达式 和 右表达式）
        Map<String, List<String>> dimensionValMap = new HashMap<>(2);

        // 将这些值的下标作为位图下标映射成一个64位数

        // key dimension, val <key: dimensionVal, val: dimensionValBit>
        Map<String, Map<String, Long>> dimensionValBitMap = new HashMap<>(2);

        for (Map.Entry<String, Function<InputUnit, String>> leftExpressionFuncEntry : LEFT_EXPRESSION_FUNC_MAP.entrySet()) {
            final String dimensionKey = leftExpressionFuncEntry.getKey();
            final Function<InputUnit, String> leftExpressionFunc = leftExpressionFuncEntry.getValue();

            // 左表达式求值
            final List<String> leftExpressionValList = inputUnits.stream().map(leftExpressionFunc).distinct().collect(Collectors.toList());

            // 右表达式求值
            final List<String> rightExpressionValList = versionRules.stream().map(VersionRule::getRuleGroupLimits)
                    .flatMap(List::stream).filter(e -> dimensionKey.equals(e.getLeftExpression()))
                    .map(GroupRuleContentDTO::getRightExpression).flatMap(List::stream).distinct().collect(Collectors.toList());
            // 用 set 再去次重
            Set<String> allValues = new HashSet<>();
            allValues.addAll(leftExpressionValList);
            allValues.addAll(rightExpressionValList);

            if (allValues.size() > DIMENSION_LIMIT) {
                throw new IllegalArgumentException("暂不支持单维度超过63个维值");
            }

            // 这里改成list是为了表达 维值有序 的概念，方便确定位图的位置
            dimensionValMap.put(dimensionKey, new ArrayList<>(allValues));

            Map<String, Long> valBitMap = new HashMap<>(16);
            for (int i = 0; i < dimensionValMap.get(dimensionKey).size(); i++) {
                valBitMap.put(dimensionValMap.get(dimensionKey).get(i), 1L << i);
            }
            dimensionValBitMap.put(dimensionKey, valBitMap);
        }
        return dimensionValBitMap;
    }

    private void versionRuleCalc(List<InputUnit> inputUnits, List<VersionRule> versionRules,
                                 VersionRule defaultRule, Map<String, Map<String, Long>> dimensionValBitMap) {
        for (VersionRule versionRule : versionRules) {

            List<InputUnit> currentRuleUnits = new ArrayList<>();

            final List<GroupRuleContentDTO> groupRuleContentObject = versionRule.getRuleGroupLimits();

            for (InputUnit inputUnit : inputUnits) {

                boolean match = true;
                for (GroupRuleContentDTO ruleContent : groupRuleContentObject) {
                    final String leftExpression = ruleContent.getLeftExpression();

                    final Map<String, Long> currentDimensionValBitMap = dimensionValBitMap.get(leftExpression);

                    final String leftDimensionVal = LEFT_EXPRESSION_FUNC_MAP.get(leftExpression).apply(inputUnit);
                    Long leftDimensionValBit = currentDimensionValBitMap.get(leftDimensionVal);


                    // 同样的右边的表达式也对应一个数(由右边的所有选中的值或运算得来）todo: 如果可选全部，那么全为1
                    final Long rightDimensionValBit = ruleContent.getRightExpression().stream()
                            .map(currentDimensionValBitMap::get).reduce(0L, (a, b) -> a | b);


                    // 支持的运算有两种，包含于 即输入单元该维度值 & 运算后 等于原先的左维度维值 不包含与 则 & 运算后 等于 0
                    final BiPredicate<Long, Long> currentOperator = OperatorType.parse(ruleContent.getOperateExpression()).getExpected();

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

                // 移除该组使用的，剩下的继续循环计算
                inputUnits.removeAll(currentRuleUnits);
            }
        }

        // 剩下的单元都进入 默认组
        if (CollectionUtils.isNotEmpty(inputUnits)) {
            //  todo 添加使用逻辑

            // 移除该组使用的，剩下的继续循环计算
        }
    }




}
