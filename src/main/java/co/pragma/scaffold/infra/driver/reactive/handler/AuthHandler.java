package co.pragma.scaffold.infra.driver.reactive.handler;

import co.pragma.scaffold.infra.driver.reactive.security.dto.AuthRequest;
import co.pragma.scaffold.infra.driver.reactive.security.dto.AuthResponse;
import co.pragma.scaffold.infra.driver.reactive.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class AuthHandler {

    private final ReactiveUserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public Mono<ServerResponse> login(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(AuthRequest.class)
                .flatMap(
        authRequest -> userDetailsService.findByUsername(authRequest.getUsername())
                        .filter(userDetails -> passwordEncoder.matches(authRequest.getPassword(), userDetails.getPassword()))
                        .map(userDetails -> jwtUtil.generateToken(userDetails.getUsername()))
                        .map(AuthResponse::new)
                        .flatMap(authResponse -> ServerResponse.ok().bodyValue(authResponse))
                        .switchIfEmpty(ServerResponse.status(HttpStatus.UNAUTHORIZED).build())
                );


    }
}
