package co.pragma.scaffold.infra.driven.webclient.adapter;

import co.pragma.scaffold.domain.ports.out.ICityOutPort;
import co.pragma.scaffold.infra.driven.webclient.dto.CityInfoDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class CityAdapter implements ICityOutPort {

    @Value("${webclient.ninja.apikey}")
    private String apiKey;

    private final WebClient webClient;

    @Override
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
}
