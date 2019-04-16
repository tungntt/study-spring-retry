package vn.tungnt.research.studyspringretry.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.LinkedHashMap;
import java.util.Map;

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
        HikariDataSource dataSource = (HikariDataSource) this.backendDSProperties().initializeDataSourceBuilder().build();
        dataSource.setPoolName("backendCP");
        dataSource.setReadOnly(true);
        return dataSource;
    }

    @Bean(name = "backendEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean backendEntityManagerFactory(@Qualifier("backendDataSource") DataSource backendDataSource) {
        LocalContainerEntityManagerFactoryBean factoryBean = new LocalContainerEntityManagerFactoryBean();
        factoryBean.setDataSource(backendDataSource);
        factoryBean.setPackagesToScan(PACKAGE_TO_SCAN);
        factoryBean.getJpaPropertyMap().putAll(this.backendJpaProperties().getProperties());
        factoryBean.getJpaPropertyMap().putAll(this.getVendorProperties());
        factoryBean.setPersistenceUnitName("backend");

        factoryBean.setJpaVendorAdapter(this.createJpaVendorAdapter());
        factoryBean.afterPropertiesSet();
        return factoryBean;
    }

    private JpaVendorAdapter createJpaVendorAdapter() {
        HibernateJpaVendorAdapter adapter = new HibernateJpaVendorAdapter();
        adapter.setGenerateDdl(this.backendJpaProperties().isGenerateDdl());
        adapter.setShowSql(this.backendJpaProperties().isShowSql());
        adapter.setDatabase(this.backendJpaProperties().getDatabase());
        adapter.setDatabasePlatform(this.backendJpaProperties().getDatabasePlatform());
        return adapter;
    }

    private Map<String, Object> getVendorProperties() {
        Map<String, Object> vendorProperties = new LinkedHashMap();
        vendorProperties.putAll(this.backendJpaProperties().getHibernateProperties(this.backendDataSource()));
        return vendorProperties;
    }

    @Bean(name = "backendTransactionManager")
    public PlatformTransactionManager backendTransactionManager(@Qualifier("backendEntityManagerFactory") EntityManagerFactory backendEntityManagerFactory) {
        return new JpaTransactionManager(backendEntityManagerFactory);
    }
}

    