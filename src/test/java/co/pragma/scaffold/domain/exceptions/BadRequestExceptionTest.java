package co.pragma.scaffold.domain.exceptions;

import co.pragma.scaffold.domain.exceptions.error.CommonErrorCode;
import co.pragma.scaffold.domain.exceptions.error.ErrorCode;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BadRequestExceptionTest {

    @Test
    void testConstructorWithMessage() {
        // Given
        String message = "Invalid input data";
        
        // When
        BadRequestException exception = new BadRequestException(message);
        
        // Then
        assertEquals(CommonErrorCode.INVALID_INPUT, exception.getErrorCode());
        assertEquals(CommonErrorCode.INVALID_INPUT.getCode(), exception.getCode());
        assertEquals(message, exception.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST.value(), exception.getStatusCode());
    }

    @Test
    void testConstructorWithErrorCode() {
        // Given
        ErrorCode errorCode = CommonErrorCode.VALIDATION_ERROR;
        
        // When
        BadRequestException exception = new BadRequestException(errorCode);
        
        // Then
        assertEquals(errorCode, exception.getErrorCode());
        assertEquals(errorCode.getCode(), exception.getCode());
        assertEquals(errorCode.getMessage(), exception.getMessage());
        assertEquals(errorCode.getStatusCode(), exception.getStatusCode());
    }

    @Test
    void testConstructorWithErrorCodeAndMessage() {
        // Given
        ErrorCode errorCode = CommonErrorCode.VALIDATION_ERROR;
        String message = "Custom validation error";
        
        // When
        BadRequestException exception = new BadRequestException(errorCode, message);
        
        // Then
        assertEquals(errorCode, exception.getErrorCode());
        assertEquals(errorCode.getCode(), exception.getCode());
        assertEquals(message, exception.getMessage());
        assertEquals(errorCode.getStatusCode(), exception.getStatusCode());
    }

    @Test
    void testConstructorWithCodeAndMessage() {
        // Given
        String code = "CUSTOM_BAD_REQUEST";
        String message = "Custom bad request error";
        
        // When
        BadRequestException exception = new BadRequestException(code, message);
        
        // Then
        assertEquals(code, exception.getCode());
        assertEquals(message, exception.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST.value(), exception.getStatusCode());
    }
}