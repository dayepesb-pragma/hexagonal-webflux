package co.pragma.scaffold.infra.driver.reactive.handler;

import co.pragma.scaffold.application.usecases.UserUseCase;
import co.pragma.scaffold.domain.exceptions.BadRequestException;
import co.pragma.scaffold.domain.model.User;
import co.pragma.scaffold.infra.driver.reactive.dto.UserRegisterDTO;
import co.pragma.scaffold.infra.driver.reactive.mapper.IUserMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class HandlerTest {

    @Mock
    private UserUseCase userUseCase;

    @Mock
    private IUserMapper userMapper;

    @Mock
    private ServerRequest serverRequest;

    @InjectMocks
    private Handler handler;

    private User user;
    private UserRegisterDTO userRegisterDTO;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId("1");
        user.setName("Test User");
        user.setEmail("test@example.com");

        userRegisterDTO = new UserRegisterDTO();
        userRegisterDTO.setName("Test User");
        userRegisterDTO.setEmail("test@example.com");
        userRegisterDTO.setPassword("password");
    }

    @Test
    void findAll_ShouldReturnUsers() {
        // Given
        when(userUseCase.findAll()).thenReturn(Flux.just(user));

        // When
        Mono<ServerResponse> response = handler.findAll(serverRequest);

        // Then
        StepVerifier.create(response)
                .expectNextMatches(serverResponse -> 
                    serverResponse.statusCode() == HttpStatus.OK)
                .verifyComplete();
    }

    @Test
    void registerUser_ShouldCreateUser() {
        // Given
        when(serverRequest.bodyToMono(UserRegisterDTO.class)).thenReturn(Mono.just(userRegisterDTO));
        when(userMapper.toUser(userRegisterDTO)).thenReturn(user);
        when(userUseCase.registerUser(user)).thenReturn(Mono.just(user));

        // When
        Mono<ServerResponse> response = handler.registerUser(serverRequest);

        // Then
        StepVerifier.create(response)
                .expectNextMatches(serverResponse -> 
                    serverResponse.statusCode() == HttpStatus.CREATED)
                .verifyComplete();
    }

    @Test
    void registerUser_WithEmptyBody_ShouldThrowBadRequestException() {
        // Given
        when(serverRequest.bodyToMono(UserRegisterDTO.class)).thenReturn(Mono.empty());

        // When
        Mono<ServerResponse> response = handler.registerUser(serverRequest);

        // Then
        StepVerifier.create(response)
                .expectErrorMatches(throwable -> 
                    throwable instanceof BadRequestException && 
                    throwable.getMessage().equals("Request body cannot be empty"))
                .verify();
    }

    @Test
    void registerUser_WithErrorInUserCase_ShouldPropagateException() {
        // Given
        when(serverRequest.bodyToMono(UserRegisterDTO.class)).thenReturn(Mono.just(userRegisterDTO));
        when(userMapper.toUser(userRegisterDTO)).thenReturn(user);
        when(userUseCase.registerUser(user)).thenReturn(Mono.error(new RuntimeException("Database error")));

        // When
        Mono<ServerResponse> response = handler.registerUser(serverRequest);

        // Then
        StepVerifier.create(response)
                .expectErrorMatches(throwable -> 
                    throwable instanceof RuntimeException && 
                    throwable.getMessage().equals("Database error"))
                .verify();
    }
}