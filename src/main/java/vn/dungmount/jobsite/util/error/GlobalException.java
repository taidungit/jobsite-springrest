package vn.dungmount.jobsite.util.error;

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

import vn.dungmount.jobsite.domain.RestResponse;

@RestControllerAdvice
public class GlobalException {
              @ExceptionHandler(value = {
            // IdInvalidException.class,
            UsernameNotFoundException.class,
            BadCredentialsException.class
    })
    public ResponseEntity<RestResponse<Object>> handleIdException(Exception ex) {
          RestResponse<Object> res=new RestResponse<Object>();
            res.setStatusCode(HttpStatus.BAD_REQUEST.value());
            res.setError(ex.getMessage());
            res.setMessage("Exception occures....");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
public ResponseEntity<RestResponse<Object>> handleValidationError(MethodArgumentNotValidException ex) {
    BindingResult result = ex.getBindingResult();
    List<FieldError> fieldErrors = result.getFieldErrors();

    RestResponse<Object> res = new RestResponse<>();
    res.setStatusCode(HttpStatus.BAD_REQUEST.value());
    res.setError("Validation error");

    // Lấy tất cả lỗi hoặc lỗi đầu tiên
    List<String> messages = fieldErrors.stream()
        .map(FieldError::getDefaultMessage)
        .collect(Collectors.toList());

    // Nếu chỉ có 1 lỗi thì trả về chuỗi, còn nhiều thì trả về danh sách
    res.setMessage(messages.size() == 1 ? messages.get(0) : messages);

    return ResponseEntity.badRequest().body(res);
}

}

