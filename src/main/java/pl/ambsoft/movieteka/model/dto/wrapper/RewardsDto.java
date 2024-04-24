package pl.ambsoft.movieteka.model.dto.wrapper;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import pl.ambsoft.movieteka.model.dto.RewardDto;

import java.util.Set;

@Builder
public record RewardsDto(@Schema(requiredMode = Schema.RequiredMode.REQUIRED) @Valid @NotEmpty Set<RewardDto> rewards) {
}
