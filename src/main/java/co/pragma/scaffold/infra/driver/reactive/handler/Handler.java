package co.pragma.scaffold.infra.driver.reactive.handler;

import co.pragma.scaffold.application.usecases.UserUseCase;
import co.pragma.scaffold.domain.exceptions.BadRequestException;
import co.pragma.scaffold.domain.exceptions.DataAccessException;
import co.pragma.scaffold.domain.model.User;
import co.pragma.scaffold.infra.driver.reactive.dto.UserRegisterDTO;
import co.pragma.scaffold.infra.driver.reactive.mapper.IUserMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "User API", description = "Operaciones relacionadas con usuarios")
public class Handler {

    private static final String ERR_EMPTY_BODY = "ERR_EMPTY_BODY";
    private static final String ERR_PROCESSING_REQUEST = "ERR_PROCESSING_REQUEST";
    
    private final UserUseCase userUseCase;
    private final IUserMapper userMapper;

    @Operation(summary = "Obtener todos los usuarios", description = "Devuelve una lista de usuarios")
    public Mono<ServerResponse> findAll(ServerRequest serverRequest) {
        Flux<User> users = userUseCase.findAll()
                .limitRate(1)
                .doOnNext(user -> {
                    try {
                        log.info("Processing user: {}", user.getId());
                        Thread.sleep(1000); // Simula delay
                    } catch (InterruptedException e) {
                        throw new DataAccessException(ERR_PROCESSING_REQUEST, "Error processing user data: " + e.getMessage());
                    }
                });

        return ServerResponse.ok()
                .body(users, User.class);
    }

    public Mono<ServerResponse> registerUser(ServerRequest request) {
        return request.bodyToMono(UserRegisterDTO.class)
                .switchIfEmpty(Mono.error(new BadRequestException(ERR_EMPTY_BODY, "Request body cannot be empty")))
                .map(userMapper::toUser)
                .flatMap(userUseCase::registerUser)
                .flatMap(user -> ServerResponse.status(HttpStatus.CREATED).bodyValue(user));
        // No need for onErrorResume as the global exception handler will catch exceptions
    }
}
