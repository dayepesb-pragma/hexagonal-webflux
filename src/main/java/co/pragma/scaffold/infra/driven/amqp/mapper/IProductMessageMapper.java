package co.pragma.scaffold.infra.driven.amqp.mapper;

import co.pragma.scaffold.domain.model.ProductModel;
import co.pragma.scaffold.infra.driven.amqp.dto.ProductMessageDto;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE
)
public interface IProductMessageMapper {

    ProductMessageDto mapToDto(ProductModel product);
}
