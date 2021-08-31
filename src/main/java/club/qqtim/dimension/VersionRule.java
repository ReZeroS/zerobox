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
     * 规则组
     */
    private List<GroupRuleContent> ruleGroupLimits;

    /**
     * 匹配成功后落到的组
     */
    private Long matchRuleMetaId;


}
