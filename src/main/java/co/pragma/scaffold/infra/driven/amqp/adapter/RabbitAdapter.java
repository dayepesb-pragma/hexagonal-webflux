package co.pragma.scaffold.infra.driven.amqp.adapter;

import co.pragma.scaffold.domain.model.ProductModel;
import co.pragma.scaffold.domain.ports.out.IMessagePort;
import co.pragma.scaffold.infra.driven.amqp.mapper.IProductMessageMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@EnableRabbit
@Slf4j
public class RabbitAdapter implements IMessagePort {

    private final RabbitTemplate rabbitTemplate;
    private final IProductMessageMapper mapper;
    private final Exchange exchange;

    @Value("${order.exchange.routingKey}")
    private String routingKey;

    public void sendAndCreateProduct(ProductModel product) {

        log.info("asdsadasdsadasdsad asdasdasdassdsa");

        rabbitTemplate.convertAndSend(exchange.getName(),
                routingKey,
                mapper.mapToDto(product)
        );

    }
}
