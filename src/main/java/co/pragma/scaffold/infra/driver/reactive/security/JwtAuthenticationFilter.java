package co.pragma.scaffold.infra.driver.reactive.security;

import co.pragma.scaffold.domain.exceptions.SecurityException;
import co.pragma.scaffold.domain.exceptions.UnauthorizedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class JwtAuthenticationFilter implements WebFilter {

    private static final String ERR_INVALID_TOKEN = "ERR_INVALID_TOKEN";
    private static final String ERR_EXPIRED_TOKEN = "ERR_EXPIRED_TOKEN";
    private static final String ERR_AUTH_FAILED = "ERR_AUTH_FAILED";

    private final JwtUtil jwtUtil;
    private final ReactiveUserDetailsService userDetailsService;

    public JwtAuthenticationFilter(JwtUtil jwtUtil, ReactiveUserDetailsService userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        return resolveTokenReactive(exchange.getRequest())
                .flatMap(token -> {
                    try {
                        String username = jwtUtil.extractUsername(token);
                        return userDetailsService.findByUsername(username)
                                .filter(userDetails -> jwtUtil.validateToken(token, userDetails))
                                .map(userDetails -> new UsernamePasswordAuthenticationToken(
                                        userDetails, null, userDetails.getAuthorities()))
                                .flatMap(authentication -> chain.filter(exchange)
                                        .contextWrite(ReactiveSecurityContextHolder.withAuthentication(authentication)))
                                .switchIfEmpty(Mono.error(new UnauthorizedException(ERR_INVALID_TOKEN, "Invalid token")));
                    } catch (Exception e) {
                        if (e.getMessage().contains("expired")) {
                            return Mono.error(new SecurityException(ERR_EXPIRED_TOKEN, "Token has expired"));
                        }
                        return Mono.error(new SecurityException(ERR_INVALID_TOKEN, "Invalid token format"));
                    }
                })
                .onErrorResume(e -> {
                    if (!(e instanceof SecurityException) && !(e instanceof UnauthorizedException)) {
                        log.error("Authentication error: {}", e.getMessage());
                    }
                    return chain.filter(exchange);
                })
                .switchIfEmpty(chain.filter(exchange)); // Continue without authentication if no token
    }

    private Mono<String> resolveTokenReactive(ServerHttpRequest request) {
        return Mono.justOrEmpty(request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION))
                .filter(bearerToken -> bearerToken.startsWith("Bearer "))
                .map(bearerToken -> bearerToken.substring(7));
    }
}
