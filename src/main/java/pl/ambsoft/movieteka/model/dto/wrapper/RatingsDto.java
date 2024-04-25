package pl.ambsoft.movieteka.model.dto.wrapper;

import lombok.Builder;
import pl.ambsoft.movieteka.model.dto.RatingDto;

import java.util.List;

@Builder
public record RatingsDto(List<RatingDto> ratings) {
}
