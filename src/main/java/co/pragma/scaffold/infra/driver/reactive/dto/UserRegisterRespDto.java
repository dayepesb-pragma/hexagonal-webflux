package co.pragma.scaffold.infra.driver.reactive.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserRegisterRespDto {
    private String name;
    private String email;
    private String message;

}
