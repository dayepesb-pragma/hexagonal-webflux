package co.pragma.scaffold.domain.ports.out;

import co.pragma.scaffold.domain.model.User;
import reactor.core.publisher.Flux;

public interface IUserRepository {

    Flux<User> findAll();
}
