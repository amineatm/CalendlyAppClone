package miu.edu.mpp.app.error;

import miu.edu.mpp.app.error.exception.BusinessException;
import miu.edu.mpp.app.error.exception.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class GlobalControllerAdvice {

    @ExceptionHandler(BusinessException.class)
    @ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY)
    public ErrorMessage resourceNotFoundException(BusinessException ex, WebRequest request) {
       log.error("resourceNotFoundException");
       return  new ErrorMessage(
                HttpStatus.UNPROCESSABLE_ENTITY,
                String.valueOf(HttpStatus.UNPROCESSABLE_ENTITY.value()),
                ex.getMessage(),
                null
           );

    }
    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public ErrorMessage resourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
        log.error("ResourceNotFoundException");
       return new ErrorMessage(
                HttpStatus.NOT_FOUND,
                String.valueOf(HttpStatus.NOT_FOUND.value()),
                ex.getMessage(),
                null
        );

    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorMessage globalExceptionHandler(Exception ex, WebRequest request) {
        log.error(ex.getClass().getName());
        log.error(ex.getMessage(),ex);
       return  new ErrorMessage(
                HttpStatus.INTERNAL_SERVER_ERROR,
                String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()),
                "ERROR INTERNO",
                null);

    }


    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object>  errorHandler(MethodArgumentNotValidException ex) {

        List<ValidationError> errors = new ArrayList<>();
        for (FieldError error : ex.getBindingResult()
                .getFieldErrors()) {
            ValidationError build = ValidationError.builder()
                    .code(error.getField())
                    .message(error.getDefaultMessage())
                    .build();
            errors.add(build);
        }
        ErrorMessage  message = new ErrorMessage(HttpStatus.BAD_REQUEST, String.valueOf(HttpStatus.BAD_REQUEST.value()) ,HttpStatus.BAD_REQUEST.name(), errors);
        return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);

    }


    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ErrorMessage> handleResponseStatusException(ResponseStatusException ex, WebRequest request) {
        log.error("ResponseStatusException");
        HttpStatus status = ex.getStatus();
        ErrorMessage errorMessage = new ErrorMessage(
                status,
                String.valueOf(status.value()),
                ex.getReason(),
                null
        );
        return new ResponseEntity<>(errorMessage, status);
    }

}
