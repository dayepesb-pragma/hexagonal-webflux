package co.pragma.scaffold.domain.exceptions;

import co.pragma.scaffold.domain.exceptions.error.CommonErrorCode;
import co.pragma.scaffold.domain.exceptions.error.ErrorCode;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class BusinessExceptionTest {

    @Test
    void testConstructorWithErrorCode() {
        // Given
        ErrorCode errorCode = CommonErrorCode.INTERNAL_ERROR;
        
        // When
        BusinessException exception = new BusinessException(errorCode);
        
        // Then
        assertEquals(errorCode, exception.getErrorCode());
        assertEquals(errorCode.getCode(), exception.getCode());
        assertEquals(errorCode.getMessage(), exception.getMessage());
        assertEquals(errorCode.getStatusCode(), exception.getStatusCode());
    }

    @Test
    void testConstructorWithErrorCodeAndMessage() {
        // Given
        ErrorCode errorCode = CommonErrorCode.INTERNAL_ERROR;
        String customMessage = "Custom error message";
        
        // When
        BusinessException exception = new BusinessException(errorCode, customMessage);
        
        // Then
        assertEquals(errorCode, exception.getErrorCode());
        assertEquals(errorCode.getCode(), exception.getCode());
        assertEquals(customMessage, exception.getMessage());
        assertEquals(errorCode.getStatusCode(), exception.getStatusCode());
    }

    @Test
    void testConstructorWithCodeMessageAndStatusCode() {
        // Given
        String code = "CUSTOM_ERROR";
        String message = "Custom error message";
        int statusCode = HttpStatus.BAD_REQUEST.value();
        
        // When
        BusinessException exception = new BusinessException(code, message, statusCode);
        
        // Then
        assertNotNull(exception.getErrorCode());
        assertEquals(code, exception.getCode());
        assertEquals(message, exception.getMessage());
        assertEquals(statusCode, exception.getStatusCode());
    }
}