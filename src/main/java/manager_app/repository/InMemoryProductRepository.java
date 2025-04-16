package manager_app.repository;

import manager_app.entity.Product;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.IntStream;

@Repository
public class InMemoryProductRepository implements ProductRepository{
    private final List<Product> products = Collections.synchronizedList(new LinkedList<>());


    @Override
    public List<Product> findAll() {
        return Collections.unmodifiableList(this.products);//Возвращаем неизменяемую копию списка
    }
    @Override
    public Product save(Product product) {
        product.setId(this.products.stream()
                .max(Comparator.comparingLong(Product::getId))
                .map(Product::getId)
                .orElse(0L) + 1);
        products.add(product);
        return product;
    }

    @Override
    public Optional<Product> findById(Long productId) {
        return this.products.stream().filter(p -> Objects.equals(p.getId(), productId)).findFirst();
    }

    @Override
    public void deleteById(Long id) {
        this.products.removeIf(product -> Objects.equals(product.getId(), id));
    }
}
