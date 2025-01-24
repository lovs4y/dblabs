package vladyslava.prazhmovska.dbrgr.web.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import vladyslava.prazhmovska.dbrgr.core.exceptions.NotFoundException;
import vladyslava.prazhmovska.dbrgr.core.exceptions.ValidationException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(exception = NotFoundException.class)
    public ResponseEntity<Object> handleNotFoundException(NotFoundException e) {
        return new ResponseEntity<>(e.getDto(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(exception = ValidationException.class)
    public ResponseEntity<Object> handleValidationException(ValidationException e) {
        return new ResponseEntity<>(e.getDto(), HttpStatus.BAD_REQUEST);
    }
}
