package pl.ambsoft.movieteka.model.dto.wrapper;

import lombok.Builder;
import pl.ambsoft.movieteka.model.dto.MovieDto;

import java.util.List;

@Builder
public record MoviesDto(List<MovieDto> movies) {
}
