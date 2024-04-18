package pl.ambsoft.movieteka.model.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.ambsoft.movieteka.model.dto.wrapper.CategoriesDto;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MovieDto {

    private Long id;

    @NotBlank
    private String title;

    @NotNull
    private Short yearOfProduction;

    @NotBlank
    private String description;

    @NotNull
    @Min(0)
    @Max(5)
    private Float review;

    @NotNull
    private CategoriesDto categoriesDto;

    private byte[] photo;
}
