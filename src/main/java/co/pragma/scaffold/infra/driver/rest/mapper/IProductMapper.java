package co.pragma.scaffold.infra.driver.rest.mapper;

import co.pragma.scaffold.domain.model.ProductModel;
import co.pragma.scaffold.infra.driver.rest.dto.req.ProductRequestDto;
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
public interface IProductMapper {

    ProductModel mapToModel(ProductRequestDto dto);
}
