package com.example.Lab_OOP.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {

    @GetMapping("/login")
    public String showLoginPage() {
        return "login"; // Без .html, Spring ищет в templates/login.html
    }

    @GetMapping("/your-page")
    public String yourPage(Model model) {
        model.addAttribute("title", "Имя пользователя"); // Передаем значение для title
        return "welcome"; // Без .html, будет искать templates/welcome.html
    }
}