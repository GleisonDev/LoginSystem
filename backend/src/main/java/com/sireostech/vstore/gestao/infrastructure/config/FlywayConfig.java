package com.sireostech.vstore.gestao.infrastructure.config;

import org.springframework.boot.autoconfigure.flyway.FlywayMigrationStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class FlywayConfig {

    @Bean
    @Profile("dev")
    public FlywayMigrationStrategy cleanMigrateStrategy() {
        return flyway -> {
            flyway.clean();
            flyway.migrate();
        };
    }

    @Bean
    @Profile("prod")
    public FlywayMigrationStrategy migrateOnlyStrategy() {
        return flyway -> flyway.migrate();
    }

    @Bean
    @Profile("default")
    public FlywayMigrationStrategy defaultStrategy() {
        return flyway -> flyway.migrate();
    }
}