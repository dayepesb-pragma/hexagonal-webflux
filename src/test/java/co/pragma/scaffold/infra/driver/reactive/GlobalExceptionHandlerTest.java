package co.pragma.scaffold.infra.driver.reactive;

import co.pragma.scaffold.domain.exceptions.*;
import co.pragma.scaffold.domain.exceptions.SecurityException;
import co.pragma.scaffold.domain.exceptions.error.CommonErrorCode;
import co.pragma.scaffold.infra.driver.reactive.config.GlobalExceptionHandler;
import co.pragma.scaffold.infra.driver.reactive.dto.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.HttpStatus;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler globalExceptionHandler;
    private ObjectMapper objectMapper;
    private MockServerWebExchange exchange;
    private DataBufferFactory dataBufferFactory;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        globalExceptionHandler = new GlobalExceptionHandler(objectMapper);
        exchange = MockServerWebExchange.from(MockServerHttpRequest.get("/api/test").build());
        dataBufferFactory = new DefaultDataBufferFactory();
    }

    @Test
    void handleBusinessException_BadRequest() {
        // Given
        BadRequestException exception = new BadRequestException(CommonErrorCode.INVALID_INPUT, "Invalid input data");

        // When
        Mono<Void> result = globalExceptionHandler.handle(exchange, exception);

        // Then
        StepVerifier.create(result)
                .verifyComplete();

        assertEquals(HttpStatus.BAD_REQUEST.value(), exchange.getResponse().getStatusCode().value());
        
        // Verify response body contains expected error details
        DataBuffer buffer = exchange.getResponse().bufferFactory().allocateBuffer();
        exchange.getResponse().writeWith(Mono.just(buffer)).block();
        String responseBody = buffer.toString(StandardCharsets.UTF_8);
        
        assertTrue(responseBody.contains(CommonErrorCode.INVALID_INPUT.getCode()));
        assertTrue(responseBody.contains("Invalid input data"));
        assertTrue(responseBody.contains("timestamp"));
    }

    @Test
    void handleBusinessException_NotFound() {
        // Given
        NotFoundException exception = new NotFoundException(CommonErrorCode.RESOURCE_NOT_FOUND, "Resource not found");

        // When
        Mono<Void> result = globalExceptionHandler.handle(exchange, exception);

        // Then
        StepVerifier.create(result)
                .verifyComplete();

        assertEquals(HttpStatus.NOT_FOUND.value(), exchange.getResponse().getStatusCode().value());
    }

    @Test
    void handleBusinessException_Unauthorized() {
        // Given
        UnauthorizedException exception = new UnauthorizedException(CommonErrorCode.UNAUTHORIZED, "Invalid credentials");

        // When
        Mono<Void> result = globalExceptionHandler.handle(exchange, exception);

        // Then
        StepVerifier.create(result)
                .verifyComplete();

        assertEquals(HttpStatus.UNAUTHORIZED.value(), exchange.getResponse().getStatusCode().value());
    }

    @Test
    void handleBusinessException_Validation() {
        // Given
        ValidationException exception = new ValidationException(CommonErrorCode.VALIDATION_ERROR, "Validation failed");

        // When
        Mono<Void> result = globalExceptionHandler.handle(exchange, exception);

        // Then
        StepVerifier.create(result)
                .verifyComplete();

        assertEquals(HttpStatus.BAD_REQUEST.value(), exchange.getResponse().getStatusCode().value());
    }

    @Test
    void handleBusinessException_DuplicateResource() {
        // Given
        DuplicateResourceException exception = new DuplicateResourceException(CommonErrorCode.RESOURCE_ALREADY_EXISTS, "Resource already exists");

        // When
        Mono<Void> result = globalExceptionHandler.handle(exchange, exception);

        // Then
        StepVerifier.create(result)
                .verifyComplete();

        assertEquals(HttpStatus.CONFLICT.value(), exchange.getResponse().getStatusCode().value());
    }

    @Test
    void handleBusinessException_DataAccess() {
        // Given
        DataAccessException exception = new DataAccessException(CommonErrorCode.DATA_ACCESS_ERROR, "Database error");

        // When
        Mono<Void> result = globalExceptionHandler.handle(exchange, exception);

        // Then
        StepVerifier.create(result)
                .verifyComplete();

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), exchange.getResponse().getStatusCode().value());
    }

    @Test
    void handleBusinessException_Security() {
        // Given
        SecurityException exception = new SecurityException(CommonErrorCode.FORBIDDEN, "Security violation");

        // When
        Mono<Void> result = globalExceptionHandler.handle(exchange, exception);

        // Then
        StepVerifier.create(result)
                .verifyComplete();

        assertEquals(HttpStatus.FORBIDDEN.value(), exchange.getResponse().getStatusCode().value());
    }

    @Test
    void handleResponseStatusException() {
        // Given
        ResponseStatusException exception = new ResponseStatusException(HttpStatus.BAD_GATEWAY, "Bad gateway");

        // When
        Mono<Void> result = globalExceptionHandler.handle(exchange, exception);

        // Then
        StepVerifier.create(result)
                .verifyComplete();

        assertEquals(HttpStatus.BAD_GATEWAY.value(), exchange.getResponse().getStatusCode().value());
    }

    @Test
    void handleGenericException() {
        // Given
        RuntimeException exception = new RuntimeException("Unexpected error");

        // When
        Mono<Void> result = globalExceptionHandler.handle(exchange, exception);

        // Then
        StepVerifier.create(result)
                .verifyComplete();

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), exchange.getResponse().getStatusCode().value());
        
        // Verify response body contains expected error details
        DataBuffer buffer = exchange.getResponse().bufferFactory().allocateBuffer();
        exchange.getResponse().writeWith(Mono.just(buffer)).block();
        String responseBody = buffer.toString(StandardCharsets.UTF_8);
        
        assertTrue(responseBody.contains(CommonErrorCode.INTERNAL_ERROR.getCode()));
        assertTrue(responseBody.contains("Unexpected error"));
    }
    
    @Test
    void handleBusinessException_WithLegacyConstructor() {
        // Given - using the legacy constructor that takes code and message directly
        BadRequestException exception = new BadRequestException("ERR_TEST_BAD_REQUEST", "Invalid input data");

        // When
        Mono<Void> result = globalExceptionHandler.handle(exchange, exception);

        // Then
        StepVerifier.create(result)
                .verifyComplete();

        assertEquals(HttpStatus.BAD_REQUEST.value(), exchange.getResponse().getStatusCode().value());
        
        // Verify response body contains expected error details
        DataBuffer buffer = exchange.getResponse().bufferFactory().allocateBuffer();
        exchange.getResponse().writeWith(Mono.just(buffer)).block();
        String responseBody = buffer.toString(StandardCharsets.UTF_8);
        
        assertTrue(responseBody.contains("ERR_TEST_BAD_REQUEST"));
        assertTrue(responseBody.contains("Invalid input data"));
    }
}