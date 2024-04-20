package pl.ambsoft.movieteka.model.dto.wrapper;

import lombok.Builder;
import pl.ambsoft.movieteka.model.dto.MovieRewardDto;

import java.util.List;

@Builder
public record MovieRewardsDto(List<MovieRewardDto> movieRewardDtoList) {
}
