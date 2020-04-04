package club.qqtim.context.support;

import lite.summer.core.io.ClassPathResource;
import lite.summer.core.io.Resource;

public class ClassPathXmlApplicationContext extends AbstractApplicationContext {


    public ClassPathXmlApplicationContext(String configFile) {
        super(configFile);
    }

    @Override
    protected Resource getResourceByPath(String path) {
        return new ClassPathResource(path, this.getBeanClassLoader());
    }

}
