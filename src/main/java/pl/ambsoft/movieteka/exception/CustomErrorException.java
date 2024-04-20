package pl.ambsoft.movieteka.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import pl.ambsoft.movieteka.exception.errors.ErrorCodes;

@Getter
@Setter
public class CustomErrorException extends RuntimeException {

    private final String field;

    private final ErrorCodes errorCode;

    private final HttpStatus httpStatus;

    public CustomErrorException(String field, ErrorCodes errorCode, HttpStatus httpStatus) {
        this.field = field;
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
    }
}
