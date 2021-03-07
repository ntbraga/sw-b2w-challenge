package br.com.b2w.b2wchallenge.config;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        List<FieldError> errors = ex.getFieldErrors();
        ApiValidationError parsed = new ApiValidationError(errors);

        return ResponseEntity.status(status).headers(headers).body(parsed);
    }

    @ExceptionHandler({ServiceException.class})
    public ResponseEntity<Object> handleServiceException(ServiceException ex, WebRequest request) {
        Map<String, Object> map = new HashMap<>();

        map.put("message", ex.getMessage());
        map.put("rootCause", ExceptionUtils.getRootCauseMessage(ex));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(map);
    }

}
