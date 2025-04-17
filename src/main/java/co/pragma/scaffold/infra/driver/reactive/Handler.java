package co.pragma.scaffold.infra.driver.reactive;

import co.pragma.scaffold.application.usecases.UserUseCase;
import co.pragma.scaffold.domain.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class Handler {

    private final UserUseCase userUseCase;

    public Mono<ServerResponse> findAll(ServerRequest serverRequest) {
        Flux<User> users = userUseCase.findAll()
                .limitRate(1)
                .doOnNext(user -> {
                    try {
                        System.out.println("Processing user: " + user.getId());
                        Thread.sleep(1000); // Simula delay
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                });

        return ServerResponse.ok()
                .body(users, User.class);
    }
}
