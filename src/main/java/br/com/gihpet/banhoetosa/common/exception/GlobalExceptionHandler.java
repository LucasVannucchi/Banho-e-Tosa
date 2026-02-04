package br.com.gihpet.banhoetosa.common.exception;

import br.com.gihpet.banhoetosa.users.domain.exceptions.DuplicateRecordException;
import br.com.gihpet.banhoetosa.users.domain.exceptions.InvalidUserException;
import br.com.gihpet.banhoetosa.users.domain.exceptions.UserAccessDeniedException;
import br.com.gihpet.banhoetosa.users.domain.exceptions.UserAlreadyExistsException;
import br.com.gihpet.banhoetosa.users.domain.exceptions.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ApiError> handleUserNotFound(UserNotFoundException ex) {
        return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ApiError> handleUserExists(UserAlreadyExistsException ex) {
        return buildResponse(HttpStatus.CONFLICT, ex.getMessage());
    }

    @ExceptionHandler(UserAccessDeniedException.class)
    public ResponseEntity<ApiError> handleAccessDenied(UserAccessDeniedException ex) {
        return buildResponse(HttpStatus.FORBIDDEN, ex.getMessage());
    }

    @ExceptionHandler(InvalidUserException.class)
    public ResponseEntity<ApiError> handleInvalidUser(InvalidUserException ex) {
        return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(DuplicateRecordException.class)
    public ResponseEntity<ApiError> handleDuplicate(DuplicateRecordException ex) {
        return buildResponse(HttpStatus.CONFLICT, ex.getMessage());
    }

    private ResponseEntity<ApiError> buildResponse(HttpStatus status, String message) {
        ApiError error = new ApiError(status.value(), message, LocalDateTime.now());
        return new ResponseEntity<>(error, status);
    }

    record ApiError(int status, String message, LocalDateTime timestamp) {}
}