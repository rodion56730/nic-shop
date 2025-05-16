package org.nicetu.nicshop.config;

import org.flywaydb.core.Flyway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.sql.SQLException;

@Configuration
public class FlywayConfig {

    @Bean
    public Flyway flyway(DataSource dataSource) throws SQLException {
        Flyway flyway = Flyway.configure()
                .dataSource(dataSource)
                .locations("classpath:db/migration")
                .baselineOnMigrate(true)
                .load();
        flyway.migrate();
        System.out.println(">>>> DB URL = " + dataSource.getConnection().getMetaData().getURL());
        return flyway;
    }
}