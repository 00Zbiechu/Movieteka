package pl.ambsoft.movieteka.model.dto.wrapper;

import lombok.Builder;
import pl.ambsoft.movieteka.model.dto.CoverDto;

import java.util.List;

@Builder
public record CoversDto(List<CoverDto> covers) {
}
