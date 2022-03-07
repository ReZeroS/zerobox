package club.qqtim;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * @version: 1.0
 * @author: rezeros.github.io
 * @date: 2020/4/3
 * @description:
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
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
