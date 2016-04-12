package com.redmonkeysoftware.sitescraper.app;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.beans.PropertyVetoException;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import javax.sql.DataSource;
import org.apache.velocity.app.VelocityEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.core.env.Environment;
import org.springframework.core.task.TaskExecutor;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.ui.velocity.VelocityEngineFactoryBean;

@Configuration
@EnableTransactionManagement
@EnableScheduling
@EnableAsync
@ComponentScan(basePackages = "com.redmonkeysoftware.sitescraper.app")
@PropertySources({
    @PropertySource("classpath:/com/redmonkeysoftware/sitescraper/app/defaults.properties")
})
public class SiteScraperAppConfiguration {

    protected final static Logger logger = LoggerFactory.getLogger(SiteScraperAppConfiguration.class.getName());
    @Autowired
    private Environment env;

    @Bean
    @Qualifier("linkProcessorTaskExecutor")
    public TaskExecutor getLinkProcessorTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(2);
        executor.setMaxPoolSize(10);
        executor.setQueueCapacity(20000);
        return executor;
    }
    
    @Bean
    public ObjectMapper getJsonMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.findAndRegisterModules();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        mapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        //SimpleModule module = new SimpleModule();
        //module.addDeserializer(LocalDate.class, new LocalDateDeserializer());
        //module.addSerializer(LocalDate.class, new LocalDateSerializer());
        //module.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer());
        //module.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer());
        //module.addDeserializer(GrantedAuthority.class, new GrantedAuthorityDeserializer());
        //mapper.registerModule(module);
        return mapper;
    }

    @Bean()
    @Qualifier("dataSource")
    public DataSource getDataSource() throws PropertyVetoException {
        HikariConfig config = new HikariConfig();
        config.setDataSourceClassName(env.getProperty("db.dataSourceClassName", "org.postgresql.ds.PGSimpleDataSource"));
        config.addDataSourceProperty("user", env.getProperty("db.user", "sitescraper"));
        config.addDataSourceProperty("password", env.getProperty("db.password", "123scraper789"));
        config.addDataSourceProperty("databaseName", env.getProperty("db.name", "sitescraper"));
        config.addDataSourceProperty("serverName", env.getProperty("db.host", "localhost"));
        config.addDataSourceProperty("portNumber", env.getProperty("db.port", Integer.class, 5432));
        config.addDataSourceProperty("ssl", env.getProperty("db.ssl", Boolean.class, Boolean.FALSE));
        return new HikariDataSource(config);
    }

    @Bean
    public DataSourceTransactionManager getTransactionManager() throws PropertyVetoException {
        DataSourceTransactionManager dstm = new DataSourceTransactionManager();
        dstm.setDataSource(getDataSource());
        return dstm;
    }

    @Bean
    public JavaMailSender getJavaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(env.getProperty("smtp.host", "smtp.mailgun.org"));
        mailSender.setPort(env.getProperty("smtp.port", Integer.class, 587));
        mailSender.setProtocol(env.getProperty("smtp.protocol", "smtp"));
        mailSender.setUsername(env.getProperty("smtp.username", "postmaster@convey365.com"));
        mailSender.setPassword(env.getProperty("smtp.password", "9royxs5fb4k8"));
        Properties props = new Properties();
        props.setProperty("mail.smtp.starttls.enable", "true");
        props.setProperty("mail.debug", "true");
        mailSender.setJavaMailProperties(props);
        return mailSender;
    }

    @Bean
    public VelocityEngine getVelocityEngine() {
        VelocityEngine engine = null;
        try {
            VelocityEngineFactoryBean engineFactoryBean = new VelocityEngineFactoryBean();
            Properties props = new Properties();
            props.setProperty("resource.loader", "class");
            props.setProperty("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
            engineFactoryBean.setVelocityProperties(props);
            engine = engineFactoryBean.createVelocityEngine();
        } catch (IOException ioe) {
            java.util.logging.Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Error creating Velocity Engine", ioe);
        }
        return engine;
    }
}
