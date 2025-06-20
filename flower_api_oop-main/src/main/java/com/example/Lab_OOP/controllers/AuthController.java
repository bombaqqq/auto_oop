package com.example.Lab_OOP.controllers;

import com.example.Lab_OOP.models.Role;
import com.example.Lab_OOP.models.User;
import com.example.Lab_OOP.repo.UserRepository;
import com.example.Lab_OOP.security.jwt.JwtUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthController(AuthenticationManager authenticationManager,
                          JwtUtil jwtUtil,
                          UserRepository userRepository,
                          PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    @ResponseBody
    public Map<String, String> loginRest(@RequestBody Map<String, String> request) {
        String login = request.get("login");
        String password = request.get("password");

        // Аутентифицируемся через AuthenticationManager
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(login, password)
        );

        // Если аутентификация прошла успешно – получаем пользователя из БД
        User user = userRepository.findByLogin(login)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Генерируем JWT-токен, в котором указываем имя и роль
        String token = jwtUtil.generateToken(user.getLogin(), user.getRole().name());

        return Map.of("token", token);
    }
    @PostMapping("/register")
    @ResponseBody
    public Map<String, String> registerRest(@RequestBody Map<String, String> request) {
        String login = request.get("login");
        String password = request.get("password");

        Optional<User> existing = userRepository.findByLogin(login);
        if (existing.isPresent()) {
            return Map.of("error", "User already exists");
        }

        User newUser = new User();
        newUser.setLogin(login);
        newUser.setPassword(password);
        newUser.setRole(Role.USER); // роль по умолчанию

        userRepository.save(newUser);
        return Map.of("message", "User registered successfully");
    }
    @GetMapping("/login-web")
    public String loginPage() {
        return "login"; // возвращает шаблон login.html
    }
    @PostMapping("/login-web")
    public String loginWeb(@RequestParam String login,
                           @RequestParam String password,
                           Model model) {
        try {
            // Пробуем аутентифицировать пользователя
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(login, password)
            );

            // Если успешно – получаем пользователя и генерируем токен
            User user = userRepository.findByLogin(login)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            String token = jwtUtil.generateToken(user.getLogin(), user.getRole().name());

            // Передаём сообщение (например, токен) в шаблон welcome.html
            //model.addAttribute("message", "Успешный вход! Ваш токен: " + token);
            return "redirect:/";

        } catch (AuthenticationException e) {
            // Если аутентификация не удалась – возвращаем login.html с ошибкой
            model.addAttribute("error", "Неверный логин или пароль");
            return "login";
        }
    }
    @GetMapping("/register-web")
    public String registerPage() {
        return "register"; // вернёт шаблон register.html (создай его при необходимости)
    }
    @PostMapping("/register-web")
    public String registerWeb(@RequestParam String login,
                              @RequestParam String password,
                              Model model) {
        Optional<User> existing = userRepository.findByLogin(login);
        if (existing.isPresent()) {
            model.addAttribute("error", "Пользователь с таким логином уже существует");
            return "register";
        }

        User newUser = new User();
        newUser.setLogin(login);
        newUser.setPassword(passwordEncoder.encode(password));
        newUser.setRole(Role.USER);

        userRepository.save(newUser);
        model.addAttribute("message", "Регистрация прошла успешно. Пожалуйста, войдите.");
        return "login"; // после регистрации переходим на страницу логина
    }
}