package club.qqtim.dimension;

import lombok.Data;

import java.util.Collection;

@Data
class Rule<OUT extends Comparable<OUT>> {

    // 左表达式
    private String leftExpression;

    // 运算表达式
    private String operateExpression;

    // 右表达式所取值集
    private Collection<OUT> rightExpression;

    // 位图
    // 年龄 1 2 3 4 5
    // 1 01 || 1 10 => a > b = 10 > 01 = 2 > 1
    // a > b

}