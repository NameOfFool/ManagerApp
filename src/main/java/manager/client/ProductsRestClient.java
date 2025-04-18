package manager.client;

import manager.entity.Product;

import java.util.List;
import java.util.Optional;


public interface ProductsRestClient {
    List<Product> findAllProducts();

    Product createProduct(String title, String details);

    Optional<Product> findProduct(long id);

    void updateProduct(long id, String title, String details);

    void deleteProduct(long id);
}
