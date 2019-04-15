package vn.tungnt.research.studyspringretry.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import vn.tungnt.research.studyspringretry.model.backend.Sensor;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

/**
 * @author java dev be team on 2019-04-13O
 * @project study-spring-retry
 */
@Configuration
@EnableJpaRepositories(transactionManagerRef = "backendTransactionManager",
                        entityManagerFactoryRef = "backendEntityManagerFactory",
                        basePackages = "vn.tungnt.research.studyspringretry.repository.backend")
public class BackendDataSourceConfig {

    private static final String PACKAGE_TO_SCAN = "vn.tungnt.research.studyspringretry.model.backend";


    @Bean(name = "backendJpaProperties")
    @ConfigurationProperties(prefix = "backend.jpa")
    @Primary
    public JpaProperties backendJpaProperties(){
       return new JpaProperties();
    }

    @Bean
    @ConfigurationProperties(prefix = "backend.datasource")
    public DataSourceProperties backendDSProperties(){
        return new DataSourceProperties();
    }

    @Bean(name = "backendDataSource")
    public DataSource backendDataSource() {
        return this.backendDSProperties().initializeDataSourceBuilder().build();
    }

    @Bean(name = "backendEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean backendEntityManagerFactory(@Qualifier("backendDataSource") DataSource backendDataSource) {
        LocalContainerEntityManagerFactoryBean factoryBean = new LocalContainerEntityManagerFactoryBean();
        factoryBean.setDataSource(backendDataSource);

        factoryBean.setJpaVendorAdapter(this.jpaVendorAdapter());
        factoryBean.setJpaPropertyMap(this.backendJpaProperties().getProperties());

        factoryBean.setPersistenceUnitName("backend");

        factoryBean.setPackagesToScan(PACKAGE_TO_SCAN);

        factoryBean.afterPropertiesSet();
        return factoryBean;
    }

    private JpaVendorAdapter jpaVendorAdapter() {
        HibernateJpaVendorAdapter adapter = new HibernateJpaVendorAdapter();
        adapter.setGenerateDdl(this.backendJpaProperties().isGenerateDdl());
        adapter.setShowSql(this.backendJpaProperties().isShowSql());
        adapter.setDatabase(this.backendJpaProperties().getDatabase());
        adapter.setDatabasePlatform(this.backendJpaProperties().getDatabasePlatform());
        return adapter;
    }

//    @Bean(name = "backendEntityManagerFactory")
//    public LocalContainerEntityManagerFactoryBean backendEntityManagerFactory(EntityManagerFactoryBuilder builder, @Qualifier("backendDataSource") DataSource dataSource) {
//        LocalContainerEntityManagerFactoryBean backend = builder.dataSource(dataSource).packages(Sensor.class).persistenceUnit("backend").build();
//        return backend;
//    }


    @Bean(name = "backendTransactionManager")
    public PlatformTransactionManager backendTransactionManager(@Qualifier("backendEntityManagerFactory") EntityManagerFactory backendEntityManagerFactory) {
        return new JpaTransactionManager(backendEntityManagerFactory);
    }
}

    