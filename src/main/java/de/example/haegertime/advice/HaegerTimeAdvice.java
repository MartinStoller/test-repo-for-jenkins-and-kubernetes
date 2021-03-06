package de.example.haegertime.advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

//Globale Behandlung von Ausnahmen
@ControllerAdvice
public class HaegerTimeAdvice {

    @ExceptionHandler(ItemNotFoundException.class)
    public ResponseEntity<APIException> handleItemNotFoundException(ItemNotFoundException e) {
        HttpStatus notfound = HttpStatus.NOT_FOUND;
        APIException apiException = new APIException(e.getMessage(), notfound);
        return new ResponseEntity<>(apiException, notfound);
    }



    @ExceptionHandler(ItemAlreadyExistsException.class)
    public ResponseEntity<APIException> handleItemExistsException(ItemAlreadyExistsException e) {
        HttpStatus badRequest = HttpStatus.BAD_REQUEST;
        APIException apiException = new APIException(e.getMessage(), badRequest);
        return new ResponseEntity<>(apiException, badRequest);
    }

    @ExceptionHandler(ListEmptyException.class)
    public ResponseEntity<APIException> handleListEmptyException(ListEmptyException e) {
        HttpStatus badRequest = HttpStatus.BAD_REQUEST;
        APIException apiException = new APIException(e.getMessage(), badRequest);
        return new ResponseEntity<>(apiException, badRequest);
    }

    @ExceptionHandler(InvalidRoleException.class)
    public ResponseEntity<APIException> handleInvalidRoleException(InvalidRoleException e) {
        HttpStatus badRequest = HttpStatus.BAD_REQUEST;
        APIException apiException = new APIException(e.getMessage(), badRequest);
        return new ResponseEntity<>(apiException, badRequest);
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
        public ResponseEntity<APIException> handleEmailAlreadyExistsException(EmailAlreadyExistsException e) {
        HttpStatus badRequest = HttpStatus.BAD_REQUEST;
        APIException apiException = new APIException(e.getMessage(), badRequest);
        return new ResponseEntity<>(apiException, badRequest);
    }

    @ExceptionHandler(InvalidInputException.class)
    public ResponseEntity<APIException> handleInvalidInputException(InvalidInputException e) {
        HttpStatus badRequest = HttpStatus.BAD_REQUEST;
        APIException apiException = new APIException(e.getMessage(), badRequest);
        return new ResponseEntity<>(apiException, badRequest);
    }
}
