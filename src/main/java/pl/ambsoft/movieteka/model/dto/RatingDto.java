package pl.ambsoft.movieteka.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record RatingDto(
        @Schema(requiredMode = Schema.RequiredMode.NOT_REQUIRED, description = "Rating ID", example = "1")
        Long id,
        @Schema(requiredMode = Schema.RequiredMode.REQUIRED, description = "Movie score points", example = "4.9")
        @NotNull
        @DecimalMax(value = "5.0")
        @DecimalMin(value = "0.0")
        Float score,
        @Schema(requiredMode = Schema.RequiredMode.NOT_REQUIRED, description = "Comment that you can add to review", example = "This is my favourite movie")
        @NotBlank
        @Size(min = 5, max = 200)
        String comment
) {
}
