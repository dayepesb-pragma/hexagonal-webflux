package co.pragma.scaffold.infra.driver.reactive.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserRegisterDTO {

    private String name;
    private String email;
    private String phoneNumber;
    private String address;
    private String identificationType;
    private String identification;
    private String password;
}
