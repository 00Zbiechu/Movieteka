package pl.ambsoft.movieteka.exception.model;

import io.swagger.v3.oas.annotations.media.Schema;
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

    @Schema(description = "field", example = "title")
    private String field;

    @Schema(description = "Error code", example = "ENTITY_DOES_NOT_EXIST")
    private ErrorCodes errorCodes;
}
