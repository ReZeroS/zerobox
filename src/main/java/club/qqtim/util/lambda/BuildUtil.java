package club.qqtim.util.lambda;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @title: GenerateUtil
 * @Author lijie78
 * @Date: 2021/1/17
 * @Version 1.0.0
 */
public final class BuildUtil {

    private BuildUtil() {
    }

    /**
     * 返回构造的层级
     * CommonUtil.generateTree(VList, V::getId, V::getParentId,  V::setChildren);
     */
    public static <T, ID> List<T> buildTree(ID rootId, List<T> list, Function<T, ID> getId,
                                            Function<T, ID> getParentId, BiConsumer<T, List<T>> setChildren) {

        final Map<ID, List<T>> parentIdMapping = list.stream().collect(Collectors.groupingBy(getParentId));


        final List<T> rootList = parentIdMapping.get(rootId);
        Queue<T> tempList = new LinkedList<>(rootList);

        while(!tempList.isEmpty()) {
            final T parent = tempList.poll();
            final ID parentId = getId.apply(parent);
            final List<T> children = parentIdMapping.getOrDefault(parentId, new ArrayList<>());
            setChildren.accept(parent, children);
            tempList.addAll(children);
        }
        return rootList;
    }


}
