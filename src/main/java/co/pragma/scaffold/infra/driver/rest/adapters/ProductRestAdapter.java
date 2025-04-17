package co.pragma.scaffold.infra.driver.rest.adapters;

import co.pragma.scaffold.domain.ports.in.IProductPort;
import co.pragma.scaffold.infra.driver.rest.dto.req.ProductRequestDto;
import co.pragma.scaffold.infra.driver.rest.mapper.IProductMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST})
public class ProductRestAdapter {

    private final IProductPort productPort;
    private final IProductMapper mapper;

    @PostMapping(path = "/create", produces = "application/json")
    public ResponseEntity<Object> createOrder(@RequestBody @Valid ProductRequestDto product) {
        productPort.validateAndCreateProduct(
                mapper.mapToModel(product)
        );
                return null;
    }
}
