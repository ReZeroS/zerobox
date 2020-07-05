package club.qqtim.agent;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.CtNewMethod;
import javassist.NotFoundException;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;

/**
 * @author tommy
 * @title: AgentMain
 * @projectName javaagent
 * @description: TODO
 * @date 2020/6/28:52 PM
 */
public class MethodStatistics {

    public static void premain(String arg, Instrumentation instrumentation) {
        final String config = arg;
        final ClassPool pool = new ClassPool();
        pool.appendSystemPath();
        instrumentation.addTransformer(new ClassFileTransformer() {
            @Override
            public byte[] transform(ClassLoader loader, String className,
                                    Class<?> classBeingRedefined, ProtectionDomain protectionDomain,
                                    byte[] classFileBuffer) {
                if (className == null || !className.replaceAll("/", ".").startsWith(config)) {
                    return null;
                }

                try {
                    className = className.replaceAll("/", ".");
                    CtClass ctClass = pool.get(className);
                    // 获取它所有方法
                    for (CtMethod declaredMethod : ctClass.getDeclaredMethods()) {
                        newMethod(declaredMethod)   ;
                    }
                    return ctClass.toBytecode();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
        });
    }

    private static CtMethod newMethod(CtMethod oldMethod) throws CannotCompileException, NotFoundException {
        CtMethod copy = CtNewMethod.copy(oldMethod, oldMethod.getDeclaringClass(), null);
        copy.setName(oldMethod.getName() + "$agent");
        oldMethod.getDeclaringClass().addMethod(copy);// 添加新方法

        if (oldMethod.getReturnType().equals(CtClass.voidType)) {
            oldMethod.setBody(String.format(voidSource, oldMethod.getName()));
        } else {
            oldMethod.setBody(String.format(source, oldMethod.getName()));
        }
        return copy;
    }

    // $$ arg1,arg2,ag3
    // $1 arg1
    // $2 arg2
    // $args Object[]

    final static String source = "{\n"
            + "        long begin = System.currentTimeMillis();"
            + "        Object result;\n"
            + "       try {\n"
            + "            result=($w)%s$agent($$);\n"
            + "        }finally{\n"
            + "        long end = System.currentTimeMillis();"
            + "        System.out.println(end - begin);"
            + "        }\n"
            + "        return ($r) result;\n"
            + "}\n";

    final static String voidSource = "{\n"
            + "        long begin = System.currentTimeMillis();"
            + "       try {\n"
            + "            %s$agent($$);\n"
            + "        }finally{\n"
            + "         long end = System.currentTimeMillis();"
            + "         System.out.println(end - begin);"
            + "        }\n"
            + "}\n";
}











