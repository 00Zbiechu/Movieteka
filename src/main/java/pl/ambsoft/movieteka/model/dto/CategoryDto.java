package pl.ambsoft.movieteka.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record CategoryDto(
        @Schema(example = "1", requiredMode = Schema.RequiredMode.NOT_REQUIRED) Long id,
        @Schema(example = "drama", requiredMode = Schema.RequiredMode.REQUIRED) @NotBlank String name) {
}
