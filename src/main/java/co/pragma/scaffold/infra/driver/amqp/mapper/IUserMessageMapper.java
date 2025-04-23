package co.pragma.scaffold.infra.driver.amqp.mapper;

import co.pragma.scaffold.domain.model.User;
import co.pragma.scaffold.infra.driver.amqp.dto.UserMessageDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface IUserMessageMapper {


    @Mapping(source = "identification", target = "identificationType", qualifiedByName = "mapIdentificationType")
    @Mapping(source = "identification", target = "identification", qualifiedByName = "mapIdentification")
    @Mapping(source = "phone", target = "phoneNumber")
    User toModel(UserMessageDto userMessageDTO);


    @Named("mapIdentificationType")
    default String mapIdentificationType(UserMessageDto.IdentificationDto identificationDTO) {
        return identificationDTO.getType();
    }

    @Named("mapIdentification")
    default String mapIdentification(UserMessageDto.IdentificationDto identificationDTO) {
        return identificationDTO.getNumber();
    }
}
