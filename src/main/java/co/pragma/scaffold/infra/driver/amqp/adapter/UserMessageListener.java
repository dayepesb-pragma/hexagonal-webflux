package co.pragma.scaffold.infra.driver.amqp.adapter;

import co.pragma.scaffold.domain.model.User;
import co.pragma.scaffold.domain.ports.in.IUserInPort;
import co.pragma.scaffold.infra.driver.amqp.dto.UserMessageDto;
import co.pragma.scaffold.infra.driver.amqp.mapper.IUserMessageMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class UserMessageListener {

    private final IUserInPort userUseCase;
    private final IUserMessageMapper mapper;



    @RabbitListener(queues = "${user.queue.name}")
    public void receiveMessage(UserMessageDto userDto) {
        log.info("Received message: {}", userDto);
        User user = mapper.toModel(userDto);

        userUseCase.registerUser(user)
                .subscribe(
                        savedUser -> log.info("User saved successfully: {}", savedUser),
                        error -> log.error("Error saving user: {}", error.getMessage())
                );

    }
}
