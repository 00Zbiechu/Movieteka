package pl.ambsoft.movieteka.mapper;

import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import pl.ambsoft.movieteka.mapper.decorator.MovieMapperDecorator;
import pl.ambsoft.movieteka.model.dto.MovieDto;
import pl.ambsoft.movieteka.model.entity.MovieEntity;

@DecoratedWith(MovieMapperDecorator.class)
@Mapper(componentModel = "spring")
public interface MovieMapper extends BaseMapper<MovieEntity, MovieDto> {

    void updateMovieEntityWithMovieDto(@MappingTarget MovieEntity movieEntity, MovieDto movieDto);
}
