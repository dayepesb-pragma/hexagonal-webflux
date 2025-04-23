package co.pragma.scaffold.infra.driven.r2dbc.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Data
@Table("\"user\"")
public class UserEntity {

    @Id
    @Column("id")
    private Long id;

    @Column("name")
    private String name;

    @Column("email")
    private String email;

    @Column("phone_number")
    private String phoneNumber;

    @Column("address")
    private String address;

    @Column("identification_type")
    private String identificationType;

    @Column("identification")
    private String identification;

}
