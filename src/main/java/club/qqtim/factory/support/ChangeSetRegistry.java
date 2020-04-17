package club.qqtim.factory.support;

import club.qqtim.definition.support.liquibase.ChangeSet;

public interface ChangeSetRegistry {

    void registerChangeSet(String id, ChangeSet changeSet);

    ChangeSet getChangeSet(String id);

}
