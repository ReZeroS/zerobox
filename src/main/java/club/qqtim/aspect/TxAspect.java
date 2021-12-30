package club.qqtim.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Slf4j
@Aspect
@EnableAspectJAutoProxy(proxyTargetClass = true, exposeProxy = true)
@Component
@Configuration
public class TxAspect {



    @Pointcut("execution(* club.qqtim.aspect.*.fillB(..)) && target(club.qqtim.aspect.BClass)")
    public void methodsToBeProfiled(){}

    @Around("methodsToBeProfiled()")
    public void test(ProceedingJoinPoint pjp) throws Throwable {
        final StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        log.info("I am not going to do anything");
        final boolean callFromhh = Arrays.stream(stackTrace)
                .anyMatch(stack ->
                        stack.getClassName().equalsIgnoreCase("club.qqtim.aspect.AbstractTestClass")
                                && stack.getMethodName().equalsIgnoreCase("fillInfo")
                );
        if (!callFromhh) {
            pjp.proceed();
        }
    }
}