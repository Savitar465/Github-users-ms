package com.inspire.msusuarios.datasources;

import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        basePackages = "com.inspire.msusuarios.dao.sgsidb",
        entityManagerFactoryRef = "sgsidbEntityManagerFactory",
        transactionManagerRef = "sgsidbTransactionManager")

public class SgsidbDataSource {
    @Value("${spring.datasource.sgsidb.jpa.properties.hibernate.dialect}")
    private String dialect;

    @Bean(name = "sgsidbDataSourceProperties")
    @ConfigurationProperties(prefix = "spring.datasource.sgsidb")
    public DataSourceProperties sgsidbDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean(name = "sgsidbEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean sgsidbEntityManagerFactory(@Qualifier("sgsidbDataSourceProperties") DataSourceProperties sgsidbDataSourceProperties) {
        LocalContainerEntityManagerFactoryBean localEntity = new LocalContainerEntityManagerFactoryBean();
        localEntity.setDataSource(sgsidbDataSourceProperties.initializeDataSourceBuilder().build());
        localEntity.setPackagesToScan("com.inspire.msusuarios.model.sgsidb");
        localEntity.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        Map<String, Object> jpaProperties = new HashMap<>();
        jpaProperties.put("hibernate.dialect", dialect);
        localEntity.setJpaPropertyMap(jpaProperties);
        return localEntity;
    }

    @Bean(name = "sgsidbTransactionManager")
    public PlatformTransactionManager sgsidbTransactionManager(
            @Qualifier("sgsidbEntityManagerFactory") EntityManagerFactory sgsidbEntityManagerFactory) {
        return new JpaTransactionManager(sgsidbEntityManagerFactory);
    }
}