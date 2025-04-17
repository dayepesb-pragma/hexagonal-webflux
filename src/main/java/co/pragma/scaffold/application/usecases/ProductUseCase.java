package co.pragma.scaffold.application.usecases;

import co.pragma.scaffold.domain.model.ProductModel;
import co.pragma.scaffold.domain.ports.in.IProductPort;
import co.pragma.scaffold.domain.ports.out.IMessagePort;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ProductUseCase implements IProductPort {

    private final IMessagePort messagePort;

    @Override
    public ProductModel validateAndCreateProduct(ProductModel product) {

        messagePort.sendAndCreateProduct(product);

        return null;
    }
}
