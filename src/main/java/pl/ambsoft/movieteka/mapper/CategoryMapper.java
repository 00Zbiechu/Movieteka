package pl.ambsoft.movieteka.mapper;

import org.mapstruct.Mapper;
import pl.ambsoft.movieteka.model.dto.CategoryDto;
import pl.ambsoft.movieteka.model.entity.CategoryEntity;

@Mapper(componentModel = "spring")
public interface CategoryMapper extends BaseMapper<CategoryEntity, CategoryDto> {
}
