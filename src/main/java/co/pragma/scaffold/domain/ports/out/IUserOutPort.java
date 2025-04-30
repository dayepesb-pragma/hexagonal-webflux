package co.pragma.scaffold.domain.ports.out;

import co.pragma.scaffold.domain.model.User;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IUserOutPort {

    Flux<User> findAll();

    Mono<User> save(User user);

    Mono<User> findByUsername(String username);
}
