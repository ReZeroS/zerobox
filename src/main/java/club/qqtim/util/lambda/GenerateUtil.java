package club.qqtim.util.lambda;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @title: GenerateUtil
 * @Author lijie78
 * @Date: 2021/1/17
 * @Version 1.0.0
 */
public final class GenerateUtil {

    private GenerateUtil() {}

    /**
     * list 要求已经排序
     * 返回构造的层级
     * CommonUtil.generateTree(constantVals, ConstantVal::getId, ConstantVal::getParentId, ConstantVal::getChildren, ConstantVal::setChildren);
     */
    public static <T> List<T> generateTree(List<T> list,
                                           Function<T, Long> getId,
                                           Function<T, Long> getParentId,
                                           Function<T, List<T>> getChildren,
                                           BiConsumer<T, List<T>> setChildren) {
        final Map<Long, T> idMapObj = list.stream()
                .collect(Collectors.toMap(getId, Function.identity(), (o, n) -> n));
        List<T> result = new ArrayList<>();

        list.forEach(obj -> {
            final Long parentId = getParentId.apply(obj);
            // getParentId.apply(obj) == null ||
            if (idMapObj.get(parentId) == null) {
                result.add(obj);
            } else {
                final T parent = idMapObj.get(parentId);
                List<T> children = getChildren.apply(parent);
                if (children == null) {
                    children = new ArrayList<>();
                }
                children.add(obj);
                setChildren.accept(parent, children);
            }
        });
        return result;
    }


}
