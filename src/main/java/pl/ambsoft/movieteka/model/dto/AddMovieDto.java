package pl.ambsoft.movieteka.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class AddMovieDto {

    @Schema(example = "Mad Max Fury Road", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank
    private String title;

    @Schema(example = "2015", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull
    private Short yearOfProduction;

    @Schema(example = "Very nice movie", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank
    private String description;

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    @Valid
    @NotEmpty
    private List<CategoryDto> categories;
}
