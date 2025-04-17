package co.pragma.scaffold.domain.model;

import lombok.*;

import java.math.BigDecimal;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductModel {

    private String name;

    private BigDecimal price;
}
