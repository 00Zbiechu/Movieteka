package pl.ambsoft.movieteka.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record MovieRewardDto(
        @Schema(example = "1", requiredMode = Schema.RequiredMode.NOT_REQUIRED) Long id,
        @Schema(example = "Oscar", requiredMode = Schema.RequiredMode.NOT_REQUIRED) String name,
        @Schema(example = "2015", requiredMode = Schema.RequiredMode.NOT_REQUIRED) LocalDate awardReceivedDate) {
}
