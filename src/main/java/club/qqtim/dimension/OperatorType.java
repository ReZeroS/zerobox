package club.qqtim.dimension;


import java.util.function.BiPredicate;

/**
 * @author lijie78
 * code 参考 t_rule_dict 表
 */

public enum OperatorType  {

    /**
     * 暂时不允许出现这种运算符，出现代表表达式有问题，仅用来免除空指针报黄警告
     */
    UNDEFINED(0, "UNDEFINED", "未定义", (a, b) -> true),

    /**
     * a 包含于 b， 即 a 是 b 的子集
     */
    INCLUDE(1, "10001", "包含于", (a, b) ->  (a & b) == a),

    /**
     * a 完全不包含于 b， 即 a b 交集为空
     */
    NOT_INCLUDE(2, "10002", "不包含于", (a, b) ->  (a & b) == 0);

    private final Integer id;
    /** 权限限制类型代码 */
    private final String code;
    /** 限制类型描述类型名称*/
    private final String name;

    private final BiPredicate<Long, Long> expected;

    OperatorType(Integer id, String code, String name, BiPredicate<Long, Long> expected) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.expected = expected;
    }


    public Integer getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }


    public BiPredicate<Long, Long> getExpected() {
        return expected;
    }

    public static OperatorType parse(String code){
        for (OperatorType operatorType : values()) {
            if (operatorType.getCode().equals(code)) {
                return operatorType;
            }
        }
        return UNDEFINED;
    }

}
