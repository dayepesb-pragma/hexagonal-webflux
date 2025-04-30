package co.pragma.scaffold.infra.driver.reactive.handler;

import co.pragma.scaffold.application.usecases.UserUseCase;
import co.pragma.scaffold.domain.model.User;
import co.pragma.scaffold.infra.driver.reactive.dto.ErrorResponse;
import co.pragma.scaffold.infra.driver.reactive.dto.UserRegisterDTO;
import co.pragma.scaffold.infra.driver.reactive.dto.UserRegisterRespDto;
import co.pragma.scaffold.infra.driver.reactive.mapper.IUserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@Slf4j
@RequiredArgsConstructor
public class Handler {

    private final UserUseCase userUseCase;
    private final IUserMapper userMapper;

    public Mono<ServerResponse> findAll(ServerRequest serverRequest) {
        Flux<User> users = userUseCase.findAll()
                .limitRate(1)
                .doOnNext(user -> {
                    try {
                        log.info("Processing user: {}", user.getId());
                        Thread.sleep(1000); // Simula delay
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                });

        return ServerResponse.ok()
                .body(users, User.class);
    }

    public Mono<ServerResponse> registerUser(ServerRequest request) {
        return request.bodyToMono(UserRegisterDTO.class)
                .map(userMapper::toUser)
                .flatMap(userUseCase::registerUser)
                .flatMap(user -> ServerResponse.status(HttpStatus.CREATED).bodyValue(user))
                .onErrorResume(e -> ServerResponse.badRequest().bodyValue(
                        new ErrorResponse("ERR_0001", "Error: "+ e.getMessage())
                ));
    }
}
