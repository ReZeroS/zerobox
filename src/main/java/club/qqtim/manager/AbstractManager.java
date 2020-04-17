package club.qqtim.manager;

import club.qqtim.config.ManagerConfig;
import club.qqtim.executor.Executor;
import club.qqtim.factory.AbstractFactory;
import club.qqtim.factory.reader.Reader;
import club.qqtim.meta.Resource;

public abstract class AbstractManager implements Manager {

    private Resource resource;

    private Reader reader;

    private AbstractFactory abstractFactory;

    private Executor executor;


    public AbstractManager(ManagerConfig managerConfig) {
        this.resource = managerConfig.getResource();
        this.reader = managerConfig.getReader();
        this.abstractFactory = managerConfig.getAbstractFactory();
        this.executor = managerConfig.getExecutor();
    }


    @Override
    public void manage() {

        // before loading configuration

        reader.loadResource(resource);

        // after loading configuration

        reader.registryFactory(abstractFactory);

        reader.loadData();

    }

    @Override
    public void execute() {
        this.executor.execute();
    }
}
