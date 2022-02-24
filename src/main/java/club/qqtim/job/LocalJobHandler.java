package club.qqtim.job;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Slf4j
@Configuration
@EnableScheduling
public class LocalJobHandler {
    


    
    @Scheduled(cron = "0/5 * * * * ?")
    private void SyncOrganizationCacheLocal(){
        log.info("每 5 秒执行一次");
    }
    
}
