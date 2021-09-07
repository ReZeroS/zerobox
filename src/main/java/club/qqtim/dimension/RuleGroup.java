package club.qqtim.dimension;

import lombok.Data;

import java.util.List;

@Data
public class RuleGroup {
    /**
     * 匹配成功后落到的组
     */
    private Long id;
    /**
     * 改组的优先级，决定是否优先匹配
     */
    private Integer priority;
    /**
     * 规则组
     */
    private List<Rule<?>> bindRuleList;

}
