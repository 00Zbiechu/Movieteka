package pl.ambsoft.movieteka.mapper;

import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import pl.ambsoft.movieteka.mapper.decorator.RatingMapperDecorator;
import pl.ambsoft.movieteka.model.dto.RatingDto;
import pl.ambsoft.movieteka.model.entity.RatingEntity;

@DecoratedWith(RatingMapperDecorator.class)
@Mapper(componentModel = "spring")
public interface RatingMapper extends BaseMapper<RatingEntity, RatingDto> {
}
