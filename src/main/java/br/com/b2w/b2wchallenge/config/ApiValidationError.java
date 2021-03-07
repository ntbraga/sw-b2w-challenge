package br.com.b2w.b2wchallenge.config;

import lombok.Getter;
import org.springframework.validation.FieldError;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
public class ApiValidationError {
    private String message;
    private List<Object> errors = new ArrayList<>();

    public ApiValidationError(List<FieldError> errors) {
        this.message = "Validation error.";
        this.errors = errors.stream().map((e) -> {
            Map<String, Object> error = new HashMap();
            error.put("field", e.getField());
            error.put("message", e.getDefaultMessage());
            error.put("rejected", e.getRejectedValue());
            return error;
        }).collect(Collectors.toList());

    }

}
