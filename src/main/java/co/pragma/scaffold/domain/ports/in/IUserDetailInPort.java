package co.pragma.scaffold.domain.ports.in;

import org.springframework.security.core.userdetails.UserDetails;
import reactor.core.publisher.Mono;

public interface IUserDetailInPort {

    Mono<UserDetails> execute(String username);
}
