package co.pragma.scaffold.infra.driven.amqp.dto;

import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProductMessageDto implements Serializable {

    @Serial
    private static final Long serialVersionUID = 1L;

    private String name;

    private BigDecimal price;
}
