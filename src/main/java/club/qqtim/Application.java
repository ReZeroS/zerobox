package club.qqtim;

import club.qqtim.dimension.InputUnit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @version: 1.0
 * @author: rezeros.github.io
 * @date: 2020/4/3
 * @description:
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class Application {

    final ConcurrentLinkedQueue<Object> objects = new ConcurrentLinkedQueue<>();


    public void init(){
        objects.offer(new InputUnit(1L));
        objects.offer(new InputUnit(2L));
        objects.offer(new InputUnit(3L));
    }

    public void sss(){
        int a[][] = new int[3][2];
        for (int i = 0; i < a.length; ++i) {
            for (int j = 0; j < a[i].length; ++j) {
                System.out.println(a[i][j]);
            }
        }
    }
    public static void main(String[] args) {
//        final Application application = new Application();
//        application.init();
//        application.sss();
//        ManagerConfig config = new ManagerConfig();
//        config.setReader(new LiquibaseXmlReader());
//        config.setAbstractFactory(new LiquibaseFactory());
//        config.setResource(new ClassPathResource("xml/master.xml"));
//        config.setExecutor(new LiquibaseValidExecutor("valid.json"));
//        Manager liquibaseManager = new LiquibaseManager(config);
//        liquibaseManager.manage();
//        liquibaseManager.execute();
//
//        while (true) {
//            String maxPrefix = "";
//            maxPrefix = "".charAt(0) + "";
//
//        }
        new SpringApplication(Application.class).run(args);
    }

}
