package co.pragma.scaffold.application.usecases;

import co.pragma.scaffold.domain.model.Identification;
import co.pragma.scaffold.domain.model.User;
import co.pragma.scaffold.domain.model.UserComp;
import co.pragma.scaffold.domain.ports.out.IUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;

@Component
@RequiredArgsConstructor
public class UserUseCase {

    private final IUserRepository userRepository;

    public Flux<User> findAll() {
        return userRepository.findAll()
                .doOnNext(user -> {
                    // Consumo api externo

                    if(user.getName().startsWith("t")) {
                        throw new RuntimeException("Error en el servicio externo");
                    }
                    Identification id = Identification.builder()
                            .type("CC")
                            .number("3213213123")
                            .build();

                    user.setIdentification(id);
                })
                .filter(user -> user.getName().equalsIgnoreCase("test"))
                .map(user -> {
                    user.setAddress("xxxxxxxxx");
                    return user;
                })
                /*
                .flatMap(user -> {
                    var userComp = UserComp.builder()
                            .data(user.getAddress() + user.getId())
                            .build();
                    return Flux.just(userComp);
                })
                */
                .onErrorResume(e -> {
                    // Manejo de errores
                    System.out.println("Error: " + e.getMessage());
                    return Flux.empty();
                })
                .doOnError(e -> {
                    // Manejo de errores
                    System.out.println("Error: " + e.getMessage());
                });
    }
}
