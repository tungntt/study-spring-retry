package vn.tungnt.research.studyspringretry.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import vn.tungnt.research.studyspringretry.model.backend.Sensor;
import vn.tungnt.research.studyspringretry.model.monitor.GatewayPercentage;
import vn.tungnt.research.studyspringretry.repository.monitor.GatewayPercentageRepository;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.Map;

/**
 * @author java dev be team on 2019-04-13
 * @project study-spring-retry
 */
@Configuration
@EnableJpaRepositories(transactionManagerRef = "monitorTransactionManager",
        entityManagerFactoryRef = "monitorEntityManagerFactory",
        basePackageClasses = {GatewayPercentageRepository.class})
public class MonitorDataSourceConfig {

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource")
    @Primary
    public DataSourceProperties monitorDSProperties() {
        return new DataSourceProperties();
    }

    @Bean(name = "monitorDataSource")
    @Primary
    public DataSource monitorDataSource(@Qualifier("monitorDSProperties") DataSourceProperties monitorDSProperties) {
        return monitorDSProperties.initializeDataSourceBuilder().build();
    }

//    @Bean(name = "monitorEntityManagerFactory")
//    public LocalContainerEntityManagerFactoryBean monitorEntityManagerFactory(@Qualifier("monitorDataSource") HikariDataSource monitorDataSource,
//                                                                              @Qualifier("monitorJpaProperties") JpaProperties monitorJpaProperties) {
//        LocalContainerEntityManagerFactoryBean factoryBean = new LocalContainerEntityManagerFactoryBean();
//        factoryBean.setDataSource(monitorDataSource);
//
//        Map<String, String> jpaProperties = monitorJpaProperties.getHibernateProperties(monitorDataSource);
//        factoryBean.setJpaPropertyMap(jpaProperties);
//        factoryBean.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
//
//        factoryBean.setPersistenceUnitName("monitorPersistence");
//
//        factoryBean.setPackagesToScan(PACKAGE_TO_SCAN);
//
//        factoryBean.afterPropertiesSet();
//        return factoryBean;
//    }

    @Bean(name = "monitorEntityManagerFactory")
    @Primary
    public LocalContainerEntityManagerFactoryBean monitorEntityManagerFactory(EntityManagerFactoryBuilder builder, @Qualifier("monitorDataSource") DataSource monitorDataSource) {
        LocalContainerEntityManagerFactoryBean monitor = builder.dataSource(monitorDataSource).packages(GatewayPercentage.class).persistenceUnit("monitor").build();
        return monitor;
    }

    @Bean(name = "monitorTransactionManager")
    @Primary
    public PlatformTransactionManager monitorTransactionManager(@Qualifier("monitorEntityManagerFactory") EntityManagerFactory monitorEntityManagerFactory) {
        return new JpaTransactionManager(monitorEntityManagerFactory);
    }
}

    