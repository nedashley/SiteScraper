package com.redmonkeysoftware.sitescraper.app;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.beans.PropertyVetoException;
import javax.sql.DataSource;
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
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.annotation.EnableTransactionManagement;

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
        executor.setCorePoolSize(10);
        executor.setMaxPoolSize(20);
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
}
