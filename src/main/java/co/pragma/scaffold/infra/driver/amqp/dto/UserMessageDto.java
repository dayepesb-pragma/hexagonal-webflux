package co.pragma.scaffold.infra.driver.amqp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserMessageDto {

    private String name;
    private String address;
    private String phone;
    private String email;
    private IdentificationDto identification;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class IdentificationDto {
        private String type;
        private String number;
    }
}
