package club.qqtim;

import club.qqtim.util.KvObject;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
public class TreeMain {


    @Data
    static  class DepartmentNode {

        private String departmentId;

        private List<DepartmentNode> children = new ArrayList<>();

        private List<String> managers;


        public Stream<DepartmentNode> allChildren() {
            return Stream.concat(Stream.of(this),
                    this.children.stream().flatMap(DepartmentNode::allChildren)); // recursion here
        }

    }

    static void combineTree(DepartmentNode rootNode, List<String> treePath, int i) {
        if (treePath.size() <= i) {
            return;
        }
        // 初始化
        final String currentDepartmentId = treePath.get(i);

        for (DepartmentNode child : rootNode.getChildren()) {
            // 找到当前部门
            if (child.getDepartmentId().equals(currentDepartmentId)) {
                combineTree(child, treePath, i + 1);
                return;
            }
        }
        // 没找到当前部门当前节点就添加该儿子
        final DepartmentNode currentDepartmentNode = new DepartmentNode();
        currentDepartmentNode.setDepartmentId(currentDepartmentId);
        rootNode.getChildren().add(currentDepartmentNode);
        combineTree(currentDepartmentNode, treePath, i + 1);
    }



    public static void main(String[] args) {
        String str = "邮箱回收、渠道搜索下载、HR手动新建、用人经理新建、职位推荐、人才库推荐";
        System.out.println(StringUtils.replace(str, "、", "\n"));
    }
}
