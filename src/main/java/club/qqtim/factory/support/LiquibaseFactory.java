package club.qqtim.factory.support;

import club.qqtim.config.Configuration;
import club.qqtim.definition.support.liquibase.ChangeSet;
import club.qqtim.executor.Executor;
import club.qqtim.factory.AbstractFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class LiquibaseFactory implements AbstractFactory, ChangeSetRegistry {



    private final Map<String, ChangeSet> changeSetMap = new ConcurrentHashMap<>(16);


    @Override
    public Configuration createConfiguration() {
        return null;
    }

    @Override
    public Executor createExecutor(Configuration configuration) {
        return null;
    }


    @Override
    public void registerChangeSet(String id, ChangeSet changeSet) {
        this.changeSetMap.put(id, changeSet);
    }

    @Override
    public ChangeSet getChangeSet(String id) {
        return changeSetMap.getOrDefault(id, null);
    }
}
