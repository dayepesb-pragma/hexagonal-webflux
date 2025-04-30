package co.pragma.scaffold.infra.driver.reactive.security;// ...existing imports...

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
public class JwtAuthenticationFilter implements WebFilter {

    private final JwtUtil jwtUtil;
    private final ReactiveUserDetailsService userDetailsService;

    public JwtAuthenticationFilter(JwtUtil jwtUtil, ReactiveUserDetailsService userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        return resolveTokenReactive(exchange.getRequest())
                .flatMap(token ->
                        Mono.just(jwtUtil.extractUsername(token))
                        .flatMap(username ->
                                userDetailsService.findByUsername(username)
                                .filter(userDetails ->
                                        jwtUtil.validateToken(token, userDetails)
                                )
                                .map(userDetails ->
                                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities())
                                )
                        )
                        .flatMap(authentication ->
                                chain.filter(exchange)
                                .contextWrite(ReactiveSecurityContextHolder.withAuthentication(authentication))
                        )
                )
                .onErrorResume(e -> {
                    return chain.filter(exchange); // Continuar sin autenticación en caso de error
                })
                .switchIfEmpty(chain.filter(exchange)); // Continuar sin autenticación si no hay token
    }

    private Mono<String> resolveTokenReactive(ServerHttpRequest request) {
        return Mono.justOrEmpty(request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION))
                .filter(bearerToken -> bearerToken.startsWith("Bearer "))
                .map(bearerToken -> bearerToken.substring(7));
    }
}
