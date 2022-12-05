package club.qqtim.tree;

import lombok.Data;

@Data
public class MpttNode {
    private String tenantId;

    private Long id;

    private String name;

    private Long parentId;

    private Integer level;

    private Integer mpttLeft;

    private Integer mpttRight;

}
