package co.pragma.scaffold.domain.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class User {
    private String id;

    private String name;

    private String email;

    private String phoneNumber;

    private String address;

    private Identification identification;
}
