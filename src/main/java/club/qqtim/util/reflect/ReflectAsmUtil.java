package club.qqtim.util.reflect;

import com.esotericsoftware.reflectasm.MethodAccess;
import lombok.Data;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@UtilityClass
public class ReflectAsmUtil {

    private static final Map<String, MethodAccess> methodAccessMap = new ConcurrentHashMap<>();


    @Data
    static class Node {
        String a;

        public int getB(){
            return 1;
        }

        public String getC(){
            return "[a]" + a;
        }
    }


    public Object invoke(Object target, String methodName, Object ...params) {
        String className = target.getClass().getName();
        MethodAccess access = methodAccessMap.computeIfAbsent(className, cn -> MethodAccess.get(target.getClass()));
        return access.invoke(target, methodName, params);
    }

    public static void main(String[] args) {
        // test
        Node node = new Node();
        node.setA("aaa");
        log.info("getA {}", invoke(node, "getA"));
        log.info("getB  {}", invoke(node, "getB"));
        log.info("getC  {}", invoke(node, "getC"));

    }




}
