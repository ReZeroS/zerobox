package club.qqtim.config;

import club.qqtim.executor.Executor;
import club.qqtim.factory.AbstractFactory;
import club.qqtim.factory.reader.Reader;
import club.qqtim.meta.Resource;
import lombok.Data;

@Data
public class ManagerConfig implements Configuration {

    private Resource resource;

    private Reader reader;

    private AbstractFactory abstractFactory;

    private Executor executor;

}
