package manager.client;

import lombok.RequiredArgsConstructor;
import manager.dto.CreateProductDto;
import manager.dto.UpdateProductDto;
import manager.entity.Product;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@RequiredArgsConstructor
public class RestClientProductsRestClient implements ProductsRestClient {

    private static final ParameterizedTypeReference<List<Product>> TYPE_REFERENCE =
            new ParameterizedTypeReference<List<Product>>() {
    };

    private final RestClient restClient;

    @Override
    public List<Product> findAllProducts() {
        return restClient
                .get()
                .uri("/catalog-api/products")
                .retrieve()
                .body(TYPE_REFERENCE);
        //Указываем тип тела ответа(указываем, что это именно список именно с дженериком Product)
    }

    @Override
    public Product createProduct(String title, String details) {
        try {
            return this.restClient
                    .post()
                    .uri("/catalog-api/products")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new CreateProductDto(title, details))
                    .retrieve()
                    .body(Product.class);
        }
        catch (HttpClientErrorException.BadRequest exception){
            ProblemDetail problemDetail = exception.getResponseBodyAs(ProblemDetail.class);
            throw new BadRequestException((List<String>)problemDetail.getProperties().get("errors"));
        }
    }

    @Override
    public Optional<Product> findProduct(long id) {
        try {
            return Optional.ofNullable(this.restClient
                    .get()
                    .uri("/catalog-api/products/{productId}", id)
                    .retrieve()
                    .body(Product.class)
            );
        }
        catch(HttpClientErrorException.NotFound exception){
            return Optional.empty();
        }
    }

    @Override
    public void updateProduct(long id, String title, String details) {
        try {
            this.restClient
                    .patch()
                    .uri("/catalog-api/products/{productId}", id)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new UpdateProductDto(title, details))
                    .retrieve()
                    .toBodilessEntity();
        }
        catch (HttpClientErrorException.BadRequest exception){
            ProblemDetail problemDetail = exception.getResponseBodyAs(ProblemDetail.class);
            throw new BadRequestException((List<String>)problemDetail.getProperties().get("errors"));
        }
    }

    @Override
    public void deleteProduct(long id) {
        try {
            this.restClient
                    .delete()
                    .uri("/catalog-api/products/{productId}", id)
                    .retrieve()
                    .toBodilessEntity();
        }
        catch(HttpClientErrorException.NotFound exception){
            throw new NoSuchElementException(exception);
        }
    }
}
