package pl.ambsoft.movieteka.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
public record CoverDto(
        @Schema(example = "1") Long id,
        @Schema(example = "Image") String name,
        @Schema(example = "JPEG") String format,
        @Schema(description = "Cover of movie") byte[] photo
) {
}
