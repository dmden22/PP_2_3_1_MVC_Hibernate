package com.dmden22.spring.mvc_hibernate.config;

import com.dmden22.spring.mvc_hibernate.model.User;
import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.thymeleaf.spring5.processor.SpringErrorClassTagProcessor;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@PropertySource("classpath:db.properties")
@PropertySource("classpath:data.sql")
@EnableTransactionManagement
@ComponentScan(value = "com.dmden22.spring.mvc_hibernate")
public class DatabaseConfig {

    @Autowired
    private Environment env;

    @Bean
    public DataSource getDataSource() {
        BasicDataSource basicDataSource = new BasicDataSource();
        basicDataSource.setDriverClassName(env.getRequiredProperty("db.driver"));
        basicDataSource.setUrl(env.getRequiredProperty("db.url"));
        basicDataSource.setUsername(env.getRequiredProperty("db.username"));
        basicDataSource.setPassword(env.getRequiredProperty("db.password"));


        basicDataSource.setInitialSize(Integer.valueOf(env.getRequiredProperty("db.initialSize")));
        basicDataSource.setMinIdle(Integer.valueOf(env.getRequiredProperty("db.minIdle")));
        basicDataSource.setMaxIdle(Integer.valueOf(env.getRequiredProperty("db.maxIdle")));
        basicDataSource.setTimeBetweenEvictionRunsMillis(Long.valueOf(env.getRequiredProperty("db.timeBetweenEvictionRunsMillis")));
        basicDataSource.setTestOnBorrow(Boolean.valueOf(env.getRequiredProperty("db.testOnBorrow")));
        basicDataSource.setValidationQuery(env.getRequiredProperty("db.validationQuery"));

        return basicDataSource;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactoryBean() {
        LocalContainerEntityManagerFactoryBean entityMF = new LocalContainerEntityManagerFactoryBean();
        entityMF.setDataSource(getDataSource());
        entityMF.setPackagesToScan(env.getRequiredProperty("db.entity.package"));
        entityMF.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        entityMF.setJpaProperties(getHibernateProperties());
        return entityMF;
    }

    private Properties getHibernateProperties() {
        Properties properties = new Properties();
        properties.setProperty("hibernate.hbm2ddl.auto", "create");
        properties.setProperty("hibernate.hbm2ddl.import_files_sql_extractor", "org.hibernate.tool.hbm2ddl.MultipleLinesSqlCommandExtractor");
        properties.setProperty("hibernate.hbm2ddl.import_files", "data.sql");
        properties.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQL8Dialect");
        return properties;
    }

    @Bean //дает EntityManagerFactory
    public PlatformTransactionManager getPlatformTransactionManager() {
        JpaTransactionManager manager = new JpaTransactionManager();
        manager.setEntityManagerFactory(entityManagerFactoryBean().getObject());
        return manager;
    }
}
