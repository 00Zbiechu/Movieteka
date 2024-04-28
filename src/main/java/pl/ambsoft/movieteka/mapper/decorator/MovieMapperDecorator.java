package pl.ambsoft.movieteka.mapper.decorator;

import com.google.common.collect.Lists;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import pl.ambsoft.movieteka.exception.CustomErrorException;
import pl.ambsoft.movieteka.exception.errors.ErrorCodes;
import pl.ambsoft.movieteka.mapper.CategoryMapper;
import pl.ambsoft.movieteka.mapper.MovieMapper;
import pl.ambsoft.movieteka.model.dto.AddMovieDto;
import pl.ambsoft.movieteka.model.dto.CategoryDto;
import pl.ambsoft.movieteka.model.dto.EditMovieDto;
import pl.ambsoft.movieteka.model.dto.MovieDto;
import pl.ambsoft.movieteka.model.entity.CategoryEntity;
import pl.ambsoft.movieteka.model.entity.MovieEntity;
import pl.ambsoft.movieteka.repository.CategoryRepository;

import java.util.List;

@NoArgsConstructor
public abstract class MovieMapperDecorator implements MovieMapper {

    @Autowired
    private MovieMapper movieMapper;

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public MovieEntity toEntity(AddMovieDto dto) {
        var movieEntity = movieMapper.toEntity(dto);
        setCategoriesForMovieEntity(dto, movieEntity);
        return movieEntity;
    }

    private void setCategoriesForMovieEntity(AddMovieDto dto, MovieEntity movieEntity) {
        List<CategoryEntity> categoryEntityList = Lists.newArrayList();
        for (CategoryDto categoryDto : dto.getCategories()) {
            categoryEntityList.add(categoryRepository.findByName(categoryDto.name()).orElseThrow(() -> new CustomErrorException("category", ErrorCodes.ENTITY_DOES_NOT_EXIST, HttpStatus.NOT_FOUND)));
        }
        movieEntity.setCategoryEntities(categoryEntityList);
    }

    @Override
    public MovieDto toDto(MovieEntity entity) {
        var movieDto = movieMapper.toDto(entity);
        setCategoriesForMovieDto(entity, movieDto);
        return movieDto;
    }

    private void setCategoriesForMovieDto(MovieEntity entity, MovieDto movieDto) {
        List<CategoryDto> categoryList = Lists.newArrayList();
        for (CategoryEntity categoryEntity : entity.getCategoryEntities()) {
            categoryList.add(categoryMapper.toDto(categoryEntity));
        }

        if (movieDto.getCategories() == null) {
            movieDto.setCategories(Lists.newArrayList());
        }

        movieDto.setCategories(categoryList);
    }

    @Override
    public void updateMovieEntityWithEditMovieDto(EditMovieDto editMovieDto, MovieEntity movieEntity) {
        movieMapper.updateMovieEntityWithEditMovieDto(editMovieDto, movieEntity);
        setCategoriesForMovieEntity(editMovieDto, movieEntity);
    }
}
