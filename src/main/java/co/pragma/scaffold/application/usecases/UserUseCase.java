package co.pragma.scaffold.application.usecases;

import co.pragma.scaffold.domain.exceptions.DataAccessException;
import co.pragma.scaffold.domain.exceptions.DuplicateResourceException;
import co.pragma.scaffold.domain.exceptions.ValidationException;
import co.pragma.scaffold.domain.model.User;
import co.pragma.scaffold.domain.ports.in.IUserInPort;
import co.pragma.scaffold.domain.ports.out.ICityOutPort;
import co.pragma.scaffold.domain.ports.out.IUserOutPort;
import co.pragma.scaffold.domain.service.EmailValidationService;
import co.pragma.scaffold.domain.service.PhoneValidationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserUseCase implements IUserInPort {

    private static final Integer NAME_MIN_LENGTH = 3;
    private static final String ERR_EMAIL_INVALID = "ERR_EMAIL_INVALID";
    private static final String ERR_EMAIL_EXISTS = "ERR_EMAIL_EXISTS";
    private static final String ERR_PHONE_INVALID = "ERR_PHONE_INVALID";
    private static final String ERR_NAME_INVALID = "ERR_NAME_INVALID";
    private static final String ERR_ID_INVALID = "ERR_ID_INVALID";
    private static final String ERR_PASSWORD_EMPTY = "ERR_PASSWORD_EMPTY";
    private static final String ERR_USER_REGISTRATION = "ERR_USER_REGISTRATION";
    
    private final IUserOutPort userRepository;
    private final EmailValidationService emailValidation;
    private final PhoneValidationService phoneValidation;
    private final IUserOutPort userPort;
    private final ICityOutPort cityPort;
    private final PasswordEncoder passwordEncoder;

    public Flux<User> findAll() {
        return userRepository.findAll()
                .filter(user -> user.getName().equalsIgnoreCase("test"))
                .map(user -> {
                    user.setAddress("xxxxxxxxx");
                    return user;
                })
                .onErrorResume(e -> {
                    log.error("Error retrieving users: {}", e.getMessage());
                    return Flux.error(new DataAccessException("Error retrieving users: " + e.getMessage()));
                });
    }

    @Override
    public Mono<User> registerUser(User user) {
        return validateUser(user)
                .map(u -> {
                    u.setName(u.getName().toUpperCase());
                    u.setPassword(passwordEncoder.encode(u.getPassword()));
                    if(u.getRole() == null) {
                        u.setRole("USER");
                    }
                    return u;
                })
                .flatMap(this::modifyLocation)
                .flatMap(userPort::save)
                .onErrorResume(e -> {
                    if (e instanceof ValidationException || e instanceof DuplicateResourceException) {
                        return Mono.error(e);
                    }
                    log.error("Error en el flujo de guardado de usuario: {}", e.getMessage());
                    return Mono.error(new DataAccessException(ERR_USER_REGISTRATION, "Error registrando el usuario: " + e.getMessage()));
                });
    }

    private Mono<User> modifyLocation(User user) {
        return Mono.just(user)
                .flatMap(u -> getLocationInfo()
                        .map(locationInfo -> {
                            String address = String.format("%s - address: %s", locationInfo, u.getAddress());
                            u.setAddress(address);
                            return u;
                        })
                        .onErrorResume(e -> {
                            log.warn("Error getting location info: {}", e.getMessage());
                            return Mono.just(u);
                        })
                );
    }

    private Mono<String> getLocationInfo() {
        return cityPort.getCityInfo("Bogota")
                .onErrorResume(e -> {
                    log.error("Error accessing city information: {}", e.getMessage());
                    return Mono.error(new DataAccessException("Error accessing city information"));
                });
    }

    private Mono<User> validateUser(User user) {
        if (user.getEmail() == null || !emailValidation.isValidEmail(user.getEmail())) {
            return Mono.error(new ValidationException(ERR_EMAIL_INVALID, "Email inválido"));
        }

        return userPort.findByUsername(user.getEmail())
                .flatMap(existingUser -> {
                    log.warn("Ya existe un usuario con este correo electrónico: {}", user.getEmail());
                    return Mono.error(new DuplicateResourceException(ERR_EMAIL_EXISTS, "Ya existe un usuario con este correo electrónico"));
                })
                .then(validateUserDetails(user))
                .thenReturn(user);
    }
    
    private Mono<User> validateUserDetails(User user) {
        return Mono.just(user)
                .filter(u -> u.getPhoneNumber() != null && phoneValidation.isValidPhone(u.getPhoneNumber()))
                .switchIfEmpty(Mono.error(new ValidationException(ERR_PHONE_INVALID, "Teléfono inválido")))
                .filter(u -> u.getName() != null && u.getName().length() >= NAME_MIN_LENGTH)
                .switchIfEmpty(Mono.error(new ValidationException(ERR_NAME_INVALID, "Nombre inválido")))
                .filter(u -> u.getIdentification() != null)
                .switchIfEmpty(Mono.error(new ValidationException(ERR_ID_INVALID, "Identificación inválida")))
                .filter(u -> u.getPassword() != null)
                .switchIfEmpty(Mono.error(new ValidationException(ERR_PASSWORD_EMPTY, "La contraseña no puede ser vacía")));
    }
}
