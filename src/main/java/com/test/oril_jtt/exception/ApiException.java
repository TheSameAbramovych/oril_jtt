package com.test.oril_jtt.exception;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ApiException extends RuntimeException {

    HttpStatus code;

    public ApiException(String message, HttpStatus code) {
        super(message);
        this.code = code;
    }
}
