package co.pragma.scaffold.infra.driven.webclient.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Value("${webclient.city.baseUrl}")
    private String cityBaseUrl;

    @Bean
    public WebClient cityApiClient() {
        return WebClient.builder()
                .baseUrl(cityBaseUrl)
                .build();
    }
}
