package co.pragma.scaffold.application.usecases;

import co.pragma.scaffold.domain.model.User;
import co.pragma.scaffold.domain.ports.in.IUserDetailInPort;
import co.pragma.scaffold.domain.ports.out.IUserOutPort;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Collections;

@RequiredArgsConstructor
@Component
public class UserDetailUseCase implements IUserDetailInPort {

    private final IUserOutPort userOutPort;

    @Override
    public Mono<UserDetails> execute(String username) {
        return userOutPort.findByUsername(username)
                .switchIfEmpty(Mono.error(new UsernameNotFoundException("Usuario no encontrado")))
                .flatMap(u -> {
                    return Mono.just(createUserDetail(u));
                });
    }

    private UserDetails createUserDetail(User user) {
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_"+user.getRole()))
        );
    }
}
