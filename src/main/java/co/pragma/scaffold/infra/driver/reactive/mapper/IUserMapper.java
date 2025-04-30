package co.pragma.scaffold.infra.driver.reactive.mapper;

import co.pragma.scaffold.domain.model.User;
import co.pragma.scaffold.infra.driver.reactive.dto.UserRegisterDTO;
import co.pragma.scaffold.infra.driver.reactive.dto.UserRegisterRespDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface IUserMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "role", constant = "USER")
    User toUser(UserRegisterDTO request);

    @Mapping(target = "message", constant = "User registered successfully")
    UserRegisterRespDto toRegistrationResponse(User user);
}
