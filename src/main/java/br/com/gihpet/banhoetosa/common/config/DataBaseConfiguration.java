package br.com.gihpet.banhoetosa.common.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class DataBaseConfiguration {

    @Value("${spring.datasource.url}")
    private String url;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;

    @Value("${spring.datasource.driver-class-name}")
    private String driver;

    @Bean
    public DataSource hikariDataSource() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(url);
        config.setUsername(username);
        config.setPassword(password);
        config.setDriverClassName(driver);

        // parâmetros de performance / saúde do pool
        config.setMaximumPoolSize(10);
        config.setMinimumIdle(1);
        config.setPoolName("gihpet-db-pool");
        config.setMaxLifetime(600000); // 10 minutos
        config.setConnectionTimeout(10000);
        config.setConnectionTestQuery("SELECT 1");

        return new HikariDataSource(config);
    }
}