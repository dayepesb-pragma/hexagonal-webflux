package co.pragma.scaffold.infra.driver.reactive.security;

import co.pragma.scaffold.domain.ports.in.IUserDetailInPort;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;


@RequiredArgsConstructor
@Component
public class UserDetailService  implements ReactiveUserDetailsService {

    private final IUserDetailInPort userDetailInPort;

    @Override
    public Mono<UserDetails> findByUsername(String username) {
        return userDetailInPort.execute(username);
    }
}
