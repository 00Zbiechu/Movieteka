package pl.ambsoft.movieteka.model.dto.wrapper;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.ambsoft.movieteka.model.dto.CategoryDto;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoriesDto {

    private Set<CategoryDto> categories;
}
