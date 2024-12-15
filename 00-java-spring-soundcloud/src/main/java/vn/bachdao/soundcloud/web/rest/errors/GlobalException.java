package vn.bachdao.soundcloud.web.rest.errors;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import vn.bachdao.soundcloud.domain.repsonse.RestResponse;

@RestControllerAdvice
public class GlobalException {
    @ExceptionHandler(value = { IdInvalidException.class })
    public ResponseEntity<RestResponse<Object>> handleIdException(IdInvalidException idInvalid) {
        RestResponse<Object> rs = new RestResponse<>();
        rs.setStatusCode(HttpStatus.BAD_REQUEST.value());
        rs.setMessage(idInvalid.getMessage());
        rs.setError("error occurs...");

        return ResponseEntity.badRequest().body(rs);
    }

    @ExceptionHandler(value = { MethodArgumentNotValidException.class })
    public ResponseEntity<RestResponse<Object>> handleIdException(MethodArgumentNotValidException exception) {
        BindingResult result = exception.getBindingResult();
        final List<FieldError> fieldErrors = result.getFieldErrors();

        RestResponse<Object> rs = new RestResponse<>();
        rs.setStatusCode(HttpStatus.BAD_REQUEST.value());

        List<String> errors = fieldErrors.stream().map(error -> error.getDefaultMessage()).collect(Collectors.toList());
        rs.setMessage(errors.size() > 1 ? errors : errors.get(0));

        rs.setError(exception.getBody().getDetail());

        return ResponseEntity.badRequest().body(rs);
    }
}