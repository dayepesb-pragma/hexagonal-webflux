package co.pragma.scaffold.domain.model;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class Identification {

    private String type;
    private String number;
}
