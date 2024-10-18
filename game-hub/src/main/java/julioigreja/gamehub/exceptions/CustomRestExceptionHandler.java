package julioigreja.gamehub.exceptions;

import julioigreja.gamehub.exceptions.custom.ApiAuthenticationException;
import julioigreja.gamehub.exceptions.custom.ApiNotFoundException;
import julioigreja.gamehub.exceptions.custom.ApiValidationException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class CustomRestExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiMessageError> handlerException(Exception e) {
        ApiMessageError error = new ApiMessageError("Internal Server Error", List.of("Internal server error..."));

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiMessageError> handlerRuntimeException(RuntimeException e) {
        ApiMessageError error = new ApiMessageError("Internal Server Error", List.of("Internal server error..."));

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiMessageError> handlerException(HttpMessageNotReadableException e) {
        ApiMessageError error = new ApiMessageError("Bad Request", List.of("Invalid JSON"));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiMessageError> handlerException(MethodArgumentNotValidException e) {
        List<String> errors = e.getBindingResult().getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.toList());

        ApiMessageError error = new ApiMessageError("Invalid data", errors);

        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(error);
    }

    @ExceptionHandler(ApiValidationException.class)
    public ResponseEntity<ApiMessageError> handlerException(ApiValidationException e) {
        ApiMessageError error = new ApiMessageError("Invalid data", List.of(e.getMessage()));

        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(error);
    }

    @ExceptionHandler(PropertyReferenceException.class)
    public ResponseEntity<ApiMessageError> handlerException(PropertyReferenceException e) {
        ApiMessageError error = new ApiMessageError("Invalid data", List.of("Invalid data"));

        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(error);
    }

    @ExceptionHandler(ApiAuthenticationException.class)
    public ResponseEntity<ApiMessageError> handlerException(ApiAuthenticationException e) {
        ApiMessageError error = new ApiMessageError("Authentication error", List.of(e.getMessage()));

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    }

    @ExceptionHandler(ApiNotFoundException.class)
    public ResponseEntity<ApiMessageError> handlerException(ApiNotFoundException e) {
        ApiMessageError error = new ApiMessageError("Not Found", List.of(e.getMessage()));

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

}
