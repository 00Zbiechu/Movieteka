package pl.ambsoft.movieteka.mapper;

import org.mapstruct.Mapper;
import pl.ambsoft.movieteka.model.dto.RewardDto;
import pl.ambsoft.movieteka.model.entity.RewardEntity;

@Mapper(componentModel = "spring")
public interface RewardMapper extends BaseMapper<RewardEntity, RewardDto> {
}
