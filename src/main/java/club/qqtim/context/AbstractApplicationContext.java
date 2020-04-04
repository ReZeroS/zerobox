package club.qqtim.context;


import java.util.List;

/**
 * @Author: ReZero
 * @Date: 3/18/19 9:40 AM
 * @Version 1.0
 */
public abstract class AbstractApplicationContext implements ApplicationContext {

    private ClassLoader classLoader;


    public AbstractApplicationContext(String configFile){
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(factory);
        Resource resource = this.getResourceByPath(configFile);
        reader.loadBeanDefinitions(resource);
        factory.setBeanClassLoader(this.getBeanClassLoader());
        registerBeanPostProcessors(factory);
    }


    protected abstract Resource getResourceByPath(String path);

    @Override
    public Object getBean(String beanID) {
        return factory.getBean(beanID);
    }

    public void setBeanClassLoader(ClassLoader beanClassLoader) {
        this.classLoader = beanClassLoader;
    }

    public ClassLoader getBeanClassLoader() {
        return this.classLoader != null ? this.classLoader : ClassUtils.getDefaultClassLoader();
    }

    protected void registerBeanPostProcessors(ConfigurableBeanFactory configurableBeanFactory){
        {
            //populateBean postProcessPropertyValues
            AutowiredAnnotationProcessor autowiredAnnotationProcessor = new AutowiredAnnotationProcessor();
            autowiredAnnotationProcessor.setBeanFactory(configurableBeanFactory);
            configurableBeanFactory.addBeanPostProcessor(autowiredAnnotationProcessor);
        }
        {
            //applyBeanPostProcessorsAfterInitialization
            AspectJAutoProxyProcessor postProcessor = new AspectJAutoProxyProcessor();
            postProcessor.setBeanFactory(configurableBeanFactory);
            configurableBeanFactory.addBeanPostProcessor(postProcessor);
        }
    }

    @Override
    public Class<?> getType(String name) throws NoSuchBeanDefinitionException {
        return this.factory.getType(name);
    }

    @Override
    public List<Object> getBeansByType(Class<?> classType) {
        return this.factory.getBeansByType(classType);
    }

}
