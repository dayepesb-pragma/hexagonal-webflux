package co.pragma.scaffold.infra.driver.reactive.handler;

import co.pragma.scaffold.domain.exceptions.BadRequestException;
import co.pragma.scaffold.domain.exceptions.UnauthorizedException;
import co.pragma.scaffold.infra.driver.reactive.security.dto.AuthRequest;
import co.pragma.scaffold.infra.driver.reactive.security.dto.AuthResponse;
import co.pragma.scaffold.infra.driver.reactive.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class AuthHandler {

    private static final String ERR_EMPTY_AUTH_REQUEST = "ERR_EMPTY_AUTH_REQUEST";
    private static final String ERR_EMPTY_USERNAME = "ERR_EMPTY_USERNAME";
    private static final String ERR_EMPTY_PASSWORD = "ERR_EMPTY_PASSWORD";
    private static final String ERR_INVALID_CREDENTIALS = "ERR_INVALID_CREDENTIALS";

    private final ReactiveUserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public Mono<ServerResponse> login(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(AuthRequest.class)
                .switchIfEmpty(Mono.error(new BadRequestException(ERR_EMPTY_AUTH_REQUEST, "Authentication request cannot be empty")))
                .flatMap(authRequest -> {
                    if (authRequest.getUsername() == null || authRequest.getUsername().isEmpty()) {
                        return Mono.error(new BadRequestException(ERR_EMPTY_USERNAME, "Username cannot be empty"));
                    }
                    if (authRequest.getPassword() == null || authRequest.getPassword().isEmpty()) {
                        return Mono.error(new BadRequestException(ERR_EMPTY_PASSWORD, "Password cannot be empty"));
                    }
                    
                    return userDetailsService.findByUsername(authRequest.getUsername())
                            .filter(userDetails -> passwordEncoder.matches(authRequest.getPassword(), userDetails.getPassword()))
                            .map(userDetails -> jwtUtil.generateToken(userDetails.getUsername()))
                            .map(AuthResponse::new)
                            .flatMap(authResponse -> ServerResponse.ok().bodyValue(authResponse))
                            .switchIfEmpty(Mono.error(new UnauthorizedException(ERR_INVALID_CREDENTIALS, "Invalid username or password")));
                });
    }
}
