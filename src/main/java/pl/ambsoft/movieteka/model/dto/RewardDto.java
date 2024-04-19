package pl.ambsoft.movieteka.model.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record RewardDto(Long id, @NotBlank String name) {
}
