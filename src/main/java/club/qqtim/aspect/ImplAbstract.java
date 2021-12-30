package club.qqtim.aspect;

import org.springframework.aop.framework.AopContext;
import org.springframework.stereotype.Component;

@Component
public class ImplAbstract extends AbstractTestClass{



    public void hh(){
        ((AbstractTestClass)(AopContext.currentProxy())).testMethod();
    }

}
