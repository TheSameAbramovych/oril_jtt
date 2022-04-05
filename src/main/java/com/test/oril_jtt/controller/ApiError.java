package com.test.oril_jtt.controller;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiError {

    private final HttpStatus status;
    private String message;

    public ApiError(HttpStatus status) {
        this.status = status;
    }

    public ApiError(HttpStatus status, Throwable ex) {
        this.status = status;
        this.message = ex.getMessage();
    }

    public ApiError(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

}
