package co.pragma.scaffold.application.usecases;

import co.pragma.scaffold.domain.model.User;
import co.pragma.scaffold.domain.ports.out.ICityOutPort;
import co.pragma.scaffold.domain.ports.out.IUserOutPort;
import co.pragma.scaffold.domain.service.EmailValidationService;
import co.pragma.scaffold.domain.service.PhoneValidationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

class UserUseCaseTest {

    @Mock
    private IUserOutPort userRepository;

    @Mock
    private EmailValidationService emailValidation;

    @Mock
    private PhoneValidationService phoneValidation;

    @Mock
    private ICityOutPort cityPort;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserUseCase userUseCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        
        // Common setup for all tests
        when(cityPort.getCityInfo(anyString())).thenReturn(Mono.just("Bogota, Colombia"));
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
    }

    @Test
    void shouldRejectUserWithExistingEmail() {
        // Arrange
        String existingEmail = "existing@example.com";
        User existingUser = User.builder()
                .email(existingEmail)
                .name("Existing User")
                .build();
        
        User newUser = User.builder()
                .email(existingEmail)
                .name("New User")
                .phoneNumber("1234567890")
                .address("123 Main St")
                .identificationType("CC")
                .identification("123456789")
                .password("password")
                .build();
        
        // Mock validations
        when(emailValidation.isValidEmail(existingEmail)).thenReturn(true);
        when(phoneValidation.isValidPhone(anyString())).thenReturn(true);
        
        // Mock repository to return an existing user with the same email
        when(userRepository.findByUsername(existingEmail)).thenReturn(Mono.just(existingUser));
        
        // Act & Assert
        StepVerifier.create(userUseCase.registerUser(newUser))
                .expectErrorMatches(throwable -> 
                    throwable instanceof IllegalArgumentException && 
                    throwable.getMessage().contains("Error registrando el usuario: Ya existe un usuario con este correo electrónico"))
                .verify();
    }

    @Test
    void shouldRegisterUserWithUniqueEmail() {
        // Arrange
        String uniqueEmail = "unique@example.com";
        User newUser = User.builder()
                .email(uniqueEmail)
                .name("New User")
                .phoneNumber("1234567890")
                .address("123 Main St")
                .identificationType("CC")
                .identification("123456789")
                .password("password")
                .build();
        
        // Mock validations
        when(emailValidation.isValidEmail(uniqueEmail)).thenReturn(true);
        when(phoneValidation.isValidPhone(anyString())).thenReturn(true);
        
        // Mock repository to return empty (no existing user with the same email)
        when(userRepository.findByUsername(uniqueEmail)).thenReturn(Mono.empty());
        
        // Mock save operation
        User savedUser = newUser.toBuilder()
                .name("NEW USER")
                .password("encodedPassword")
                .role("USER")
                .address("Bogota, Colombia - address: 123 Main St")
                .build();
        when(userRepository.save(newUser)).thenReturn(Mono.just(savedUser));
        
        // Act & Assert
        StepVerifier.create(userUseCase.registerUser(newUser))
                .expectNextMatches(user -> 
                    user.getName().equals("NEW USER") && 
                    user.getEmail().equals(uniqueEmail) &&
                    user.getRole().equals("USER"))
                .verifyComplete();
    }
}