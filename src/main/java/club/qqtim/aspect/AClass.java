package club.qqtim.aspect;

import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.framework.AopContext;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AClass extends BClass {



    public void fill(){
        ((BClass)(AopContext.currentProxy())).fillInfo();
    }

    @Override
    void fillB() {
        log.info("我被执行了");
//        super.fillB();
    }
}
