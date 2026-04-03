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
        basePackages = "com.inspire.msusuarios.dao.usuarios",
        entityManagerFactoryRef = "usuariosEntityManagerFactory",
        transactionManagerRef = "usuariosTransactionManager")
public class UsuariosDataSource {

    @Value("${spring.datasource.usuarios.jpa.properties.hibernate.dialect}")
    private String dialect;

    @Bean(name = "usuariosDataSourceProperties")
    @ConfigurationProperties(prefix = "spring.datasource.usuarios")
    public DataSourceProperties usuariosDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean(name = "usuariosEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean usuariosEntityManagerFactory(@Qualifier("usuariosDataSourceProperties") DataSourceProperties usuariosDataSourceProperties) {
        LocalContainerEntityManagerFactoryBean localEntity = new LocalContainerEntityManagerFactoryBean();
        localEntity.setDataSource(usuariosDataSourceProperties.initializeDataSourceBuilder().build());
        localEntity.setPackagesToScan("com.inspire.msusuarios.model.usuarios");
        localEntity.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        Map<String, Object> jpaProperties = new HashMap<>();
        jpaProperties.put("hibernate.dialect", dialect);
        localEntity.setJpaPropertyMap(jpaProperties);
        return localEntity;
    }

    @Bean(name = "usuariosTransactionManager")
    public PlatformTransactionManager usuariosTransactionManager(
            @Qualifier("usuariosEntityManagerFactory") EntityManagerFactory usuariosEntityManagerFactory) {
        return new JpaTransactionManager(usuariosEntityManagerFactory);
    }
}
