package club.qqtim.factory;

import club.qqtim.config.Configuration;
import club.qqtim.executor.Executor;

public interface AbstractFactory  {

    Configuration createConfiguration();

    Executor createExecutor(Configuration configuration);

}
