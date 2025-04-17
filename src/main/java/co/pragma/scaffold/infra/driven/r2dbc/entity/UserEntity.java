package co.pragma.scaffold.infra.driven.r2dbc.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("user")
@Data
public class UserEntity {

    @Id
    @Column("id")
    private String id;

    @Column("name")
    private String name;

    @Column("email")
    private String email;

    @Column("phone_number")
    private String phoneNumber;

    @Column("address")
    private String address;
}
