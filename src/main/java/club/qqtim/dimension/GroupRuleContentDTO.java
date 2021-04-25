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
public class GroupRuleContentDTO {

    private String leftExpression;

    private String operateExpression;

    private List<String> rightExpression;


}

