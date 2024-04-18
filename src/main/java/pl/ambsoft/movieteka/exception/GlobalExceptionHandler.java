package pl.ambsoft.movieteka.exception;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import pl.ambsoft.movieteka.exception.errors.ErrorCodes;
import pl.ambsoft.movieteka.exception.model.ErrorDto;
import pl.ambsoft.movieteka.exception.wrapper.ErrorList;

import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomErrorException.class)
    public ResponseEntity<ErrorList> handleCustomErrorException(CustomErrorException ex) {
        ErrorDto errorDTO = ErrorDto.builder().field(ex.getField()).errorCodes(ex.getErrorCode()).build();
        return ResponseEntity.status(ex.getHttpStatus()).body(ErrorList.builder().errorDtoList(List.of(errorDTO)).build());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorList> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        List<ErrorDto> errorList = new ArrayList<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            var fieldName = ((FieldError) error).getField();
            errorList.add(ErrorDto.builder().field(fieldName).errorCodes(ErrorCodes.FIELD_ERROR).build());
        });
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorList.builder().errorDtoList(errorList).build());
    }
}