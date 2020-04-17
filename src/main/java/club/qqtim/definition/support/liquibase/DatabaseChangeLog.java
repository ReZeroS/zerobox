package club.qqtim.definition.support.liquibase;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * @version: 1.0
 * @author: rezeros.github.io
 * @date: 2020/4/3
 * @description:
 */
public class DatabaseChangeLog {

    private static final Logger logger = LoggerFactory.getLogger(DatabaseChangeLog.class);

    private List<ChangeSet> changeSets;

    public DatabaseChangeLog() {
        this.changeSets = new ArrayList<>();
//        loadChangeSet();
    }


    public List<ChangeSet> getChangeSets() {
        return changeSets;
    }




}
