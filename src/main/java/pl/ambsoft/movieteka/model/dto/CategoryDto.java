package pl.ambsoft.movieteka.model.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record CategoryDto(String id, @NotBlank String name) {
}
