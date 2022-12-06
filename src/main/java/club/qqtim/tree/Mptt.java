package club.qqtim.tree;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Mptt {

    private MpttMapper baseMapper;

    public void saveMpttNode(MpttNodeReq req) {
        if (req.getParentId().equals(req.getId())) {
            throw new BusinessException(BaseResCode.PARAM_ERROR, "不能设置自己为父节点，请重新选择");
        }
        MpttNode mpttNode;
        if (req.getId() == null) {
            mpttNode = createMpttNode(req);
        } else {
            mpttNode = updateMpttNode(req);
        }
    }

    public MpttNode updateMpttNode(MpttNodeReq req) {
        // 仅更新名称之类的，不改变结构关系
        final MpttNode mpttNode = new MpttNode(req);
        MpttNode existOne = getOne(new QueryWrapper<MpttNode>().lambda()
                .eq(MpttNode::getId, req.getId()).eq(MpttNode::getTenantId, req.getTenantId()));
        if (existOne.getParentId().equals(mpttNode.getParentId())) {
            updateById(mpttNode);
            return mpttNode;
        }
        // 先查出该节点以及改节点的子节点是哪些
        List<MpttNode> allSubMpttNode = findAllSubMpttNode(req.getTenantId(), existOne.getMpttLeft(), existOne.getMpttRight());


        MpttNode newParentNode = getOne(new QueryWrapper<MpttNode>().lambda()
                .eq(MpttNode::getTenantId, req.getTenantId()).eq(MpttNode::getParentId, req.getParentId()));


        // 相当于先删除 后新建
        Integer mpttLeft = existOne.getMpttLeft();
        Integer mpttRight = existOne.getMpttRight();
        int diff = mpttRight - mpttLeft + 1;
        // 该节点摘掉，后继节点肯定 decr 了
        baseMapper.decrLeft(mpttNode.getTenantId(), mpttLeft, diff);
        baseMapper.decrRight(mpttNode.getTenantId(), mpttRight, diff);

        // 然后更新新的talentPool, 相当于在新 parent 下新增，然后改掉之前的 allSubTalentPool left right
        // 因为 mptt 本质是链式的，所以只要求出 变更前后 节点的左值变化，其所有子节点的左右值都应用这个变化即可


        // 取父节点下直接子节点的最右节点
        Integer maxRight = baseMapper.findMaxRight(req.getTenantId(), req.getParentId());
        // 如果 没有 最大的 right 的
        if (maxRight == null) {
            // 因为是更新，所以一定不是空树
            // 空树： maxRight == null && newParentNode == null
            // 就是说 maxRight == null 的时候 newParentNode 一定不等于null
            // 因为 newParentNode 为null， 只有为根节点的时候才存在
            // 而这种情况下，又只有 非根节点直接子节点 以外的节点迁移过来才算，根直接子节点再迁移相当于没改parentId，不参与更新了
            // 既然有这种节点，说明 非根节点直接子节点 必不为空，那么maxRight 必不等于 null
            //
            // 综上， newParentNode 这里一定不为 null，并且其不存在直接子节点
            mpttNode.setMpttLeft(newParentNode.getMpttLeft() + 1);
            compareAndSetChange(mpttNode, existOne, allSubMpttNode);

            // 后续节点直接加diff即可
            baseMapper.incrLeft(req.getTenantId(), newParentNode.getMpttRight(), diff);
            // 注意的是该父节点添加新节点后其本身的右值也变化了，所以是 EQ
            baseMapper.incrRight(req.getTenantId(), newParentNode.getMpttLeft(), diff);
            // 最后强刷算好的新位置
            updateById(mpttNode);
            if (CollectionUtils.isNotEmpty(allSubMpttNode)) {
                updateBatchById(allSubMpttNode);
            }
        } else {
            // 否则为同级情况
            // + 1作为左节点 + 2 作为右节点
            mpttNode.setMpttLeft(maxRight + 1);
            compareAndSetChange(mpttNode, existOne, allSubMpttNode);
            // 后续节点直接加diff即可
            baseMapper.incrLeft(req.getTenantId(), maxRight, diff);
            // 注意的是该父节点添加新节点后其本身的右值也变化了，所以是 EQ
            baseMapper.incrRight(req.getTenantId(), maxRight, diff);
            // 最后强刷算好的新位置
            updateById(mpttNode);
            if (CollectionUtils.isNotEmpty(allSubMpttNode)) {
                updateBatchById(allSubMpttNode);
            }
        }
        return mpttNode;
    }

    private void compareAndSetChange(MpttNode mpttNode, MpttNode existOne, List<MpttNode> allSubMpttNode) {
        mpttNode.setMpttRight(mpttNode.getMpttLeft() + (existOne.getMpttRight() - existOne.getMpttLeft()));
        // 子节点应用这个 change
        int change = mpttNode.getMpttLeft() - existOne.getMpttLeft();
        int changeLevel = mpttNode.getLevel() - existOne.getLevel();
        for (MpttNode subNode : allSubMpttNode) {
            subNode.setMpttLeft(subNode.getMpttLeft() + change);
            subNode.setMpttRight(subNode.getMpttRight() + change);
            subNode.setLevel(subNode.getLevel() + changeLevel);
        }
    }

    public MpttNode createMpttNode(MpttNodeReq req) {
        MpttNode parentMpttNode = getOne(new QueryWrapper<MpttNode>().lambda()
                .eq(MpttNode::getTenantId, req.getTenantId()).eq(MpttNode::getId, req.getParentId()));

        final MpttNode mpttNode = new MpttNode(req);

        // set level
        final int level = Optional.ofNullable(parentMpttNode).map(MpttNode::getLevel).orElse(0) + 1;
        mpttNode.setLevel(level);

        // 假设每次添加新节点都是往右侧添加

        // 取父节点下直接子节点的最右节点
        Integer maxRight = baseMapper.findMaxRight(req.getTenantId(), req.getParentId());
        // 如果 没有 最大的 right 的
        if (maxRight == null) {
            // 空树, parentId = -1
            if (parentMpttNode == null) {
                mpttNode.setMpttLeft(0);
                mpttNode.setMpttRight(1);
                save(mpttNode);
            } else {
                // 该 parent 没有任何子节点
                // 更新所有后继节点 + 2
                baseMapper.incrLeft(req.getTenantId(), parentMpttNode.getMpttLeft(), 2);
                baseMapper.incrRight(req.getTenantId(), parentMpttNode.getMpttLeft(), 2);
                mpttNode.setMpttLeft(parentMpttNode.getMpttLeft() + 1);
                mpttNode.setMpttRight(mpttNode.getMpttLeft() + 1);
                save(mpttNode);
            }
        } else {
            // 否则为同级情况
            // + 1作为左节点 + 2 作为右节点
            mpttNode.setMpttLeft(maxRight + 1);
            mpttNode.setMpttRight(maxRight + 2);
            baseMapper.incrLeft(req.getTenantId(), maxRight, 2);
            baseMapper.incrRight(req.getTenantId(), maxRight, 2);
            save(mpttNode);
        }
        return mpttNode;

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteMpttNode(String tenantId, Long id) {
        // 校验是否关联简历
        // 查处所有子节点
        MpttNode mpttNode = getOne(new QueryWrapper<MpttNode>().lambda()
                .eq(MpttNode::getTenantId, tenantId).eq(MpttNode::getId, id));
        List<MpttNode> allSubMpttNodeList = findAllSubMpttNode(tenantId, mpttNode.getMpttLeft(), mpttNode.getMpttRight());

        List<Long> relatedIds = new ArrayList<>();
        relatedIds.add(mpttNode.getId());
        relatedIds.addAll(allSubMpttNodeList.stream().map(MpttNode::getId).collect(Collectors.toList()));

        Integer mpttLeft = mpttNode.getMpttLeft();
        Integer mpttRight = mpttNode.getMpttRight();
        int diff = mpttRight - mpttLeft + 1;

        baseMapper.decrLeft(tenantId, mpttLeft, diff);
        baseMapper.decrRight(tenantId, mpttRight, diff);

        removeByIds(relatedIds);

    }

    private List<MpttNode> findAllSubMpttNode(String tenantId, int left, int right) {
        return list(new QueryWrapper<MpttNode>().lambda().eq(MpttNode::getTenantId, tenantId)
                .gt(MpttNode::getMpttLeft, left).lt(MpttNode::getMpttRight, right));
    }
}
