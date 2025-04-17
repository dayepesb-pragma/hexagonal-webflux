package co.pragma.scaffold.domain.ports.in;

import co.pragma.scaffold.domain.model.ProductModel;

public interface IProductPort {

    ProductModel validateAndCreateProduct(ProductModel product);

}
