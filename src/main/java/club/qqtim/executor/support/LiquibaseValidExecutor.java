package club.qqtim.executor.support;

import club.qqtim.executor.Executor;

/**
 * @version: 1.0
 * @author: jie.li13@hand-china.com
 * @date: 2020/4/3
 * @description:
 */
public class LiquibaseValidExecutor implements Executor {

    private String configPath;

    public LiquibaseValidExecutor(String configPath) {
        this.configPath = configPath;
    }

    @Override
    public void execute() {
        loadMasterConfiguration();


    }

    private void loadMasterConfiguration() {


    }
}
