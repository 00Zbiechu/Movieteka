package pl.ambsoft.movieteka.mapper;

import org.mapstruct.Mapper;
import pl.ambsoft.movieteka.model.dto.CoverDto;
import pl.ambsoft.movieteka.model.entity.CoverEntity;

@Mapper(componentModel = "spring")
public interface CoverMapper extends BaseMapper<CoverEntity, CoverDto> {
}
