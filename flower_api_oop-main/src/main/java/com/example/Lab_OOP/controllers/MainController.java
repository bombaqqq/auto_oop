package com.example.Lab_OOP.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import jakarta.servlet.http.HttpServletRequest;
@Controller
public class MainController {
    @GetMapping("/")
    public String home(HttpServletRequest request, Model model) {
        model.addAttribute("request", request);
        return "home";
    }

}