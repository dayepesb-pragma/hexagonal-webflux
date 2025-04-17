package co.pragma.scaffold.domain.ports.out;

import co.pragma.scaffold.domain.model.ProductModel;

public interface IMessagePort {

    void sendAndCreateProduct(ProductModel product);
}
