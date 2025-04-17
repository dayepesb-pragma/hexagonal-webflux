package co.pragma.scaffold.infra.driven.r2dbc.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "adapters.r2dbc")
public record PostgresqlConnectionProperties (
        String host,
        Integer port,
        String database,
        String schema,
        String username,
        String password
){
}