package pl.ambsoft.movieteka.exception.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.ambsoft.movieteka.exception.errors.ErrorCodes;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ErrorDto {

    private String field;

    private ErrorCodes errorCodes;
}
