package club.qqtim.factory.reader;

import club.qqtim.factory.AbstractFactory;
import club.qqtim.meta.Resource;

public interface Reader {

    void loadChangeSets();

    void loadResource(Resource resource);

    void registryFactory(AbstractFactory abstractFactory);
}
