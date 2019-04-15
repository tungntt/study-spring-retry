package vn.tungnt.research.studyspringretry;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

//@SpringBootApplication(exclude = {
//        DataSourceAutoConfiguration.class,
//        DataSourceTransactionManagerAutoConfiguration.class,
//        HibernateJpaAutoConfiguration.class })
@SpringBootApplication
@EnableAsync
public class StudySpringRetryApplication {



    public static void main(String[] args) {
        SpringApplication.run(StudySpringRetryApplication.class, args);
    }

}
