package club.qqtim.dimension;

/**
 * @title: GroupRuleContentDTO
 * @Author lijie78
 * @Date: 2021/3/11
 * @Version 1.0.0
 */

import lombok.Data;

import java.util.List;

/**
 * 输入单元分群规则
 */
@Data
public class GroupRuleContent {

    // 左表达式
    private String leftExpression;

    // 运算表达式
    private String operateExpression;

    // 右表达式所取值集
    private List<String> rightExpression;


}

