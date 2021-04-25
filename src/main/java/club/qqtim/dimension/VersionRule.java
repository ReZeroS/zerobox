package club.qqtim.dimension;

import lombok.Data;

import java.util.List;

/**
 * @title: VersionRule
 * @Author lijie78
 * @Date: 2021/3/11
 * @Version 1.0.0
 */
@Data
public class VersionRule {

    /**
    *  Id 不可被当作引用外表字段
     *  每次保存版本时都会清空数据，生成新的ID
     */
    private Long id;

    private Long version;


    private String groupPriority;

    private Integer groupPriorityOrder;



    private String groupRuleContent;

    private List<GroupRuleContentDTO> ruleGroupLimits;


    /**
     * 适用规则
     */
    private Long matchRuleMetaId;

    /**
     * 适用规则名称
     */
    private String matchRuleMetaName;




}
