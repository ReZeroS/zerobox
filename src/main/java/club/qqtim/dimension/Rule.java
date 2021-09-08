package club.qqtim.dimension;

import lombok.Data;

import java.util.Collection;
import java.util.List;

@Data
class Rule<OUT extends Comparable<OUT>> {

    // 左表达式
    private String leftExpression;

    // 运算表达式
    private String operateExpression;

    // 右表达式所取值集
    private Collection<OUT> rightExpression;

}