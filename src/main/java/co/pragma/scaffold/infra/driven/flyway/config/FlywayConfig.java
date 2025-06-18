package co.pragma.scaffold.infra.driven.flyway.config;

import org.springframework.boot.autoconfigure.flyway.FlywayProperties;
import org.springframework.boot.autoconfigure.r2dbc.R2dbcProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.flywaydb.core.Flyway;

@Configuration
@EnableConfigurationProperties({R2dbcProperties.class, FlywayProperties.class})
class DatabaseConfig {

    @Bean(initMethod = "migrate")
    public Flyway flyway(FlywayProperties flywayProperties, R2dbcProperties r2dbcProperties) {
        System.out.println("Initializing Flyway with properties: " + flywayProperties.getUrl()
                + ", User: " + flywayProperties.getUser()
                + ", Password: " + flywayProperties.getPassword()
                + ", Schemas: " + flywayProperties.getSchemas()
                + ", Locations: " + flywayProperties.getLocations());
        return Flyway.configure()
                .dataSource(
                        flywayProperties.getUrl(),
                        flywayProperties.getUser(),
                        flywayProperties.getPassword()
                )
                .schemas(flywayProperties.getSchemas().toArray(new String[0]))
                .locations(flywayProperties.getLocations().toArray(String[]::new))
                .load();
    }
}
