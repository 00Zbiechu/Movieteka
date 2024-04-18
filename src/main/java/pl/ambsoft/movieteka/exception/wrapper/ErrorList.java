package pl.ambsoft.movieteka.exception.wrapper;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.ambsoft.movieteka.exception.model.ErrorDto;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ErrorList {

    private List<ErrorDto> errorDtoList;
}
