package pl.ambsoft.movieteka.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class MovieDto extends EditMovieDto {

    @Schema(requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private byte[] photo;
}
