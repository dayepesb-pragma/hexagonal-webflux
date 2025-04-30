package co.pragma.scaffold.domain.ports.in;

import co.pragma.scaffold.domain.model.User;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IUserInPort {

    Flux<User> findAll();

    Mono<User> registerUser(User user);
}
