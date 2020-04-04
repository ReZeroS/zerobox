package club.qqtim.definition.support.liquibase.reader;

import club.qqtim.definition.support.liquibase.ChangeSet;
import club.qqtim.definition.support.liquibase.DatabaseChangeLog;

/**
 * @version: 1.0
 * @author: jie.li13@hand-china.com
 * @date: 2020/4/3
 * @description:
 */
public interface DatabaseChangeLogReader {

    DatabaseChangeLog read();
}
