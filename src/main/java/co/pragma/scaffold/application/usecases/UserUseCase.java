package co.pragma.scaffold.application.usecases;

import co.pragma.scaffold.domain.model.User;
import co.pragma.scaffold.domain.ports.in.IUserInPort;
import co.pragma.scaffold.domain.ports.out.ICityOutPort;
import co.pragma.scaffold.domain.ports.out.IUserOutPort;
import co.pragma.scaffold.domain.service.EmailValidationService;
import co.pragma.scaffold.domain.service.PhoneValidationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserUseCase implements IUserInPort {

    private static final Integer NAME_MIN_LENGTH = 3;
    private final IUserOutPort userRepository;
    private final EmailValidationService emailValidation;
    private final PhoneValidationService phoneValidation;
    private final IUserOutPort userPort;
    private final ICityOutPort cityPort;

    public Flux<User> findAll() {
        return userRepository.findAll()
                .filter(user -> user.getName().equalsIgnoreCase("test"))
                .map(user -> {
                    user.setAddress("xxxxxxxxx");
                    return user;
                });
    }

    @Override
    public Mono<User> saveUser(User user) {
        return validateUser(user)
                .map( u -> {
                    u.setName(u.getName().toUpperCase());
                    return u;
                })
                .flatMap(this::modifyLocation)
                .flatMap(userPort::save)
                .onErrorResume(e -> {
                    log.error("Error en el flujo de guardado de usuario: {}", e.getMessage());
                    return Mono.error(new IllegalArgumentException("error guardando el usuario: "+ e.getMessage()));
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
                );
    }

    private Mono<String> getLocationInfo() {
        return cityPort.getCityInfo("Bogota");
    }

    private Mono<User> validateUser(User user) {
        return Mono.just(user)
                .filter(u -> u.getEmail() != null &&  emailValidation.isValidEmail(u.getEmail()))
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Email invalido")))
                .filter(u -> u.getPhoneNumber() != null && phoneValidation.isValidPhone(u.getPhoneNumber()))
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Telefono invalido")))
                .filter(u -> u.getName() != null && u.getName().length()>=NAME_MIN_LENGTH)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("nombre invalido")))
                .filter(u -> u.getIdentification() != null)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("identificación invalida")));
    }
}
