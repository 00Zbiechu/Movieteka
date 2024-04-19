package pl.ambsoft.movieteka.model.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;

@Builder
public record RewardDto(Long id, @NotEmpty String name) {
}