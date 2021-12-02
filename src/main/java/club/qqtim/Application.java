package club.qqtim;

import club.qqtim.config.ManagerConfig;
import club.qqtim.executor.support.LiquibaseValidExecutor;
import club.qqtim.factory.reader.LiquibaseXmlReader;
import club.qqtim.factory.support.LiquibaseFactory;
import club.qqtim.manager.Manager;
import club.qqtim.manager.support.LiquibaseManager;
import club.qqtim.meta.ClassPathResource;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @version: 1.0
 * @author: rezeros.github.io
 * @date: 2020/4/3
 * @description:
 */
@SpringBootApplication
public class Application {


    public static void main(String[] args) {
//        ManagerConfig config = new ManagerConfig();
//        config.setReader(new LiquibaseXmlReader());
//        config.setAbstractFactory(new LiquibaseFactory());
//        config.setResource(new ClassPathResource("xml/master.xml"));
//        config.setExecutor(new LiquibaseValidExecutor("valid.json"));
//        Manager liquibaseManager = new LiquibaseManager(config);
//        liquibaseManager.manage();
//        liquibaseManager.execute();
//
        new SpringApplication(Application.class).run(args);
    }

}
