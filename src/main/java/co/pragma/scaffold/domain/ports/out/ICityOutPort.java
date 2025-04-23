package co.pragma.scaffold.domain.ports.out;

import reactor.core.publisher.Mono;

public interface ICityOutPort {

    Mono<String> getCityInfo(String cityName);
}
