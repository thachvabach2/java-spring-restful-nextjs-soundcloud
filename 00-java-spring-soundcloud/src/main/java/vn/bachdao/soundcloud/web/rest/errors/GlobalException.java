package vn.bachdao.soundcloud.web.rest.errors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.servlet.http.HttpServletResponse;
import vn.bachdao.soundcloud.domain.repsonse.RestResponse;

@RestControllerAdvice
public class GlobalException {
    @ExceptionHandler({ IdInvalidException.class })
    public ResponseEntity<RestResponse<Object>> handleIdException(IdInvalidException idInvalid,
            HttpServletResponse response) {
        RestResponse<Object> rs = new RestResponse<>();
        rs.setStatusCode(HttpStatus.BAD_REQUEST.value());
        rs.setMessage(idInvalid.getMessage());
        rs.setError("error occurs...");

        return ResponseEntity.badRequest().body(rs);
    }
}
