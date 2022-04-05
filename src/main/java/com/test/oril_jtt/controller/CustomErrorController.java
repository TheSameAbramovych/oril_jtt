package com.test.oril_jtt.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RestController
public class CustomErrorController implements ErrorController {

    @RequestMapping("error")
    public ApiError error(HttpServletResponse response) {
        return new ApiError(HttpStatus.resolve(response.getStatus()));
    }
}
