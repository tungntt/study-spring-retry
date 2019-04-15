package vn.tungnt.research.studyspringretry.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

/**
 * @author java dev be team on 2019-04-15
 * @project study-spring-retry
 */
@Configuration
public class TaskExecutorConfig {

    @Bean(name = "gwImporterThreadPool")
    @ConfigurationProperties(prefix = "gw.importer")
    public Executor gwImporterThreadPoolTaskExecutor(){
        return new ThreadPoolTaskExecutor();
    }
}

    