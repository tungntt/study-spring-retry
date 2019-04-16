package vn.tungnt.research.studyspringretry.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import vn.tungnt.research.studyspringretry.repository.monitor.GatewayPercentageRepository;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.LinkedHashMap;
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

    private static final String PACKAGE_TO_SCAN = "vn.tungnt.research.studyspringretry.model.monitor";

    @Bean(name = "monitorJpaProperties")
    @ConfigurationProperties(prefix = "monitor.jpa")
    @Primary
    public JpaProperties monitorJpaProperties(){
        return new JpaProperties();
    }

    @Bean
    @ConfigurationProperties(prefix = "monitor.datasource")
    @Primary
    public DataSourceProperties monitorDSProperties() {
        return new DataSourceProperties();
    }

    @Bean(name = "monitorDataSource")
    @Primary
    public DataSource monitorDataSource() {
        HikariDataSource dataSource = (HikariDataSource) this.monitorDSProperties().initializeDataSourceBuilder().build();
        dataSource.setPoolName("monitorCP");
        dataSource.setReadOnly(true);
        return dataSource;
    }

    @Bean(name = "monitorEntityManagerFactory")
    @Primary
    public LocalContainerEntityManagerFactoryBean monitorEntityManagerFactory(@Qualifier("monitorDataSource") DataSource monitorDataSource) {
        LocalContainerEntityManagerFactoryBean factoryBean = new LocalContainerEntityManagerFactoryBean();
        factoryBean.setDataSource(monitorDataSource);
        factoryBean.setPackagesToScan(PACKAGE_TO_SCAN);
        factoryBean.getJpaPropertyMap().putAll(this.monitorJpaProperties().getProperties());
        factoryBean.getJpaPropertyMap().putAll(this.getVendorProperties());
        factoryBean.setPersistenceUnitName("monitor");

        factoryBean.setJpaVendorAdapter(this.createJpaVendorAdapter());
        factoryBean.afterPropertiesSet();
        return factoryBean;
    }

    private JpaVendorAdapter createJpaVendorAdapter() {
        HibernateJpaVendorAdapter adapter = new HibernateJpaVendorAdapter();
        adapter.setGenerateDdl(this.monitorJpaProperties().isGenerateDdl());
        adapter.setShowSql(this.monitorJpaProperties().isShowSql());
        adapter.setDatabase(this.monitorJpaProperties().getDatabase());
        adapter.setDatabasePlatform(this.monitorJpaProperties().getDatabasePlatform());
        return adapter;
    }

    private Map<String, Object> getVendorProperties() {
        Map<String, Object> vendorProperties = new LinkedHashMap();
        vendorProperties.putAll(this.monitorJpaProperties().getHibernateProperties(this.monitorDataSource()));
        return vendorProperties;
    }


    @Bean(name = "monitorTransactionManager")
    @Primary
    public PlatformTransactionManager monitorTransactionManager(@Qualifier("monitorEntityManagerFactory") EntityManagerFactory monitorEntityManagerFactory) {
        return new JpaTransactionManager(monitorEntityManagerFactory);
    }
}

    