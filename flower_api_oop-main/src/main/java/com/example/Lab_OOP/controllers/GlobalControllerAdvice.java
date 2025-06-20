package com.example.Lab_OOP.controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalControllerAdvice {
    @ModelAttribute("request")
    public HttpServletRequest addHttpServletRequestToModel(HttpServletRequest request) {
        return request;
    }
}
