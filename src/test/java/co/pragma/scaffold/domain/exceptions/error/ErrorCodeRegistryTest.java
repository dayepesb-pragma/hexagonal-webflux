package co.pragma.scaffold.domain.exceptions.error;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class ErrorCodeRegistryTest {

    @Test
    void testLookupExistingErrorCode() {
        // Given
        String code = CommonErrorCode.RESOURCE_NOT_FOUND.getCode();
        
        // When
        Optional<ErrorCode> errorCode = ErrorCodeRegistry.lookup(code);
        
        // Then
        assertTrue(errorCode.isPresent());
        assertEquals(code, errorCode.get().getCode());
        assertEquals(CommonErrorCode.RESOURCE_NOT_FOUND.getMessage(), errorCode.get().getMessage());
        assertEquals(CommonErrorCode.RESOURCE_NOT_FOUND.getStatusCode(), errorCode.get().getStatusCode());
    }
    
    @Test
    void testLookupNonExistingErrorCode() {
        // Given
        String code = "NON_EXISTING_CODE";
        
        // When
        Optional<ErrorCode> errorCode = ErrorCodeRegistry.lookup(code);
        
        // Then
        assertFalse(errorCode.isPresent());
    }
    
    @Test
    void testGetOrDefault() {
        // Given
        String code = "NON_EXISTING_CODE";
        ErrorCode defaultErrorCode = CommonErrorCode.INTERNAL_ERROR;
        
        // When
        ErrorCode errorCode = ErrorCodeRegistry.getOrDefault(code, defaultErrorCode);
        
        // Then
        assertEquals(defaultErrorCode, errorCode);
    }
    
    @Test
    void testIsRegistered() {
        // Given
        String existingCode = CommonErrorCode.RESOURCE_NOT_FOUND.getCode();
        String nonExistingCode = "NON_EXISTING_CODE";
        
        // When & Then
        assertTrue(ErrorCodeRegistry.isRegistered(existingCode));
        assertFalse(ErrorCodeRegistry.isRegistered(nonExistingCode));
    }
    
    @Test
    void testGetAllErrorCodes() {
        // When
        Map<String, ErrorCode> allErrorCodes = ErrorCodeRegistry.getAllErrorCodes();
        
        // Then
        assertNotNull(allErrorCodes);
        assertFalse(allErrorCodes.isEmpty());
        
        // Check that all CommonErrorCode values are in the map
        for (CommonErrorCode errorCode : CommonErrorCode.values()) {
            assertTrue(allErrorCodes.containsKey(errorCode.getCode()));
        }
    }
    
    @Test
    void testRegisterCustomErrorCode() {
        // Given
        ErrorCode customErrorCode = new ErrorCode() {
            @Override
            public String getCode() {
                return "CUSTOM_ERROR_CODE";
            }
            
            @Override
            public String getMessage() {
                return "Custom error message";
            }
            
            @Override
            public int getStatusCode() {
                return HttpStatus.I_AM_A_TEAPOT.value();
            }
            
            @Override
            public ErrorCategory getCategory() {
                return ErrorCategory.SYSTEM;
            }
        };
        
        // When
        ErrorCodeRegistry.register(customErrorCode);
        
        // Then
        Optional<ErrorCode> registeredErrorCode = ErrorCodeRegistry.lookup("CUSTOM_ERROR_CODE");
        assertTrue(registeredErrorCode.isPresent());
        assertEquals("CUSTOM_ERROR_CODE", registeredErrorCode.get().getCode());
        assertEquals("Custom error message", registeredErrorCode.get().getMessage());
        assertEquals(HttpStatus.I_AM_A_TEAPOT.value(), registeredErrorCode.get().getStatusCode());
    }
    
    @Test
    void testRegisterDuplicateErrorCode() {
        // Given
        ErrorCode duplicateErrorCode = new ErrorCode() {
            @Override
            public String getCode() {
                return CommonErrorCode.RESOURCE_NOT_FOUND.getCode(); // Using an existing code
            }
            
            @Override
            public String getMessage() {
                return "Duplicate error message";
            }
            
            @Override
            public int getStatusCode() {
                return HttpStatus.I_AM_A_TEAPOT.value();
            }
            
            @Override
            public ErrorCategory getCategory() {
                return ErrorCategory.SYSTEM;
            }
        };
        
        // When & Then
        assertThrows(IllegalArgumentException.class, () -> ErrorCodeRegistry.register(duplicateErrorCode));
    }
}