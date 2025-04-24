package co.pragma.scaffold.infra.driven.webclient.adapter;

import co.pragma.scaffold.domain.ports.out.ICityOutPort;
import co.pragma.scaffold.infra.driven.webclient.dto.CityInfoDto;
import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
@Slf4j
public class CityAdapter implements ICityOutPort {

    private static final String CITY_SERVICE = "cityService";

    @Value("${webclient.ninja.apikey}")
    private String apiKey;

    private final WebClient webClient;

    @Override
    @CircuitBreaker(name = CITY_SERVICE, fallbackMethod = "getCityFallback")
    @Retry(name = CITY_SERVICE)
    @Bulkhead(name = CITY_SERVICE)
    @TimeLimiter(name = CITY_SERVICE)
    public Mono<String> getCityInfo(String cityName) {
        return webClient.get()
                .uri(uriBuilder ->
                    uriBuilder
                            .queryParam("name", cityName)
                            .build()
                )
                .header("X-Api-Key", apiKey)
                .retrieve()
                .bodyToFlux(CityInfoDto.class)
                .next()
                .map(cityInfoDto ->
                        String.format("Ciudad: %s - Pais: %s - lat: %f - lon: %f", cityInfoDto.getName(), cityInfoDto.getCountry(), cityInfoDto.getLatitude(), cityInfoDto.getLongitude()));
    }

    private Mono<String> getCityFallback(String cityName, Exception e) {
        log.warn("Fallback: active {}", e.getMessage());
        return Mono.just("no se pudo obtener los datos de la ciudad :(");
    }
}
