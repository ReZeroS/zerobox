package club.qqtim.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@EnableAspectJAutoProxy(proxyTargetClass = true)
@Component
@Configuration
public class TxAspect {



    @Pointcut("execution(* club.qqtim.aspect.AbstractTestClass.testMethod(..))")
    public void methodsToBeProfiled(){}

    @Around("methodsToBeProfiled()")
    public void test(ProceedingJoinPoint pjp) throws Throwable {
        log.info("I am not going to do anything");
        pjp.proceed();
    }
}