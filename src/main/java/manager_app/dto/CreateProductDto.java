package manager_app.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateProductDto(
        @NotNull
        @Size(min=3, max=50)
        String title,
        @Size(max=50)
        String details) {
}
