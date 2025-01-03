package vn.bachdao.soundcloud.web.rest.errors;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import vn.bachdao.soundcloud.service.dto.repsonse.RestResponse;

@RestControllerAdvice
public class GlobalException {

    @ExceptionHandler(value = {
            UsernameNotFoundException.class,
            BadCredentialsException.class,
            IdInvalidException.class })
    public ResponseEntity<RestResponse<Object>> handleException(Exception ex) {
        RestResponse<Object> rs = new RestResponse<>();
        rs.setStatusCode(HttpStatus.BAD_REQUEST.value());
        rs.setMessage(ex.getMessage());
        rs.setError("Exception occurs...");

        return ResponseEntity.badRequest().body(rs);
    }

    @ExceptionHandler(value = { MethodArgumentNotValidException.class })
    public ResponseEntity<RestResponse<Object>> handleValidException(MethodArgumentNotValidException exception) {
        BindingResult result = exception.getBindingResult();
        final List<FieldError> fieldErrors = result.getFieldErrors();

        RestResponse<Object> rs = new RestResponse<>();
        rs.setStatusCode(HttpStatus.BAD_REQUEST.value());

        List<String> errors = fieldErrors.stream().map(error -> error.getDefaultMessage()).collect(Collectors.toList());
        rs.setMessage(errors.size() > 1 ? errors : errors.get(0));

        rs.setError(exception.getBody().getDetail());

        return ResponseEntity.badRequest().body(rs);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<RestResponse<Object>> handleNotFoundException(NoResourceFoundException ex) {
        RestResponse<Object> res = new RestResponse<Object>();
        res.setStatusCode(HttpStatus.NOT_FOUND.value());
        res.setMessage(ex.getMessage());
        res.setError("404 Not Found, URL may not exist ...");

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
    }

    @ExceptionHandler(value = {
            StorageException.class })
    public ResponseEntity<RestResponse<Object>> handleFileUploadException(Exception ex) {
        RestResponse<Object> rs = new RestResponse<>();
        rs.setStatusCode(HttpStatus.BAD_REQUEST.value());
        rs.setMessage(ex.getMessage());
        rs.setError("Exception upload file...");

        return ResponseEntity.badRequest().body(rs);
    }
}
