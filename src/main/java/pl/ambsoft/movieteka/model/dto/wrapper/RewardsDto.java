package pl.ambsoft.movieteka.model.dto.wrapper;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import pl.ambsoft.movieteka.model.dto.RewardDto;

import java.util.Set;

@Builder
public record RewardsDto(@NotNull Set<RewardDto> rewards) {
}
