package club.qqtim.tree;

import org.apache.ibatis.annotations.Param;

public interface MpttMapper {
    Integer findMaxRight(@Param("tenantId") String tenantId, @Param("parentId") Long parentId);

    void decrLeft(@Param("tenantId") String tenantId, @Param("left") int left, @Param("diff") int diff);


    void decrRight(@Param("tenantId") String tenantId, @Param("right") int right, @Param("diff") int diff);

    void incrLeft(@Param("tenantId")String tenantId, @Param("incrBase") Integer right, @Param("diff") int diff);

    void incrRight(@Param("tenantId")String tenantId, @Param("incrBase") Integer right, @Param("diff") int diff);


    void incrEqRight(@Param("tenantId")String tenantId, @Param("right") Integer right, @Param("diff") int diff);

}
