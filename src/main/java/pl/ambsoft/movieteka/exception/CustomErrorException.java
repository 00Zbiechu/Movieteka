package pl.ambsoft.movieteka.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import pl.ambsoft.movieteka.exception.errors.ErrorCodes;

@Getter
@Setter
@AllArgsConstructor
public class CustomErrorException extends RuntimeException {

    private final String field;

    private final ErrorCodes errorCode;

    private final HttpStatus httpStatus;
}
