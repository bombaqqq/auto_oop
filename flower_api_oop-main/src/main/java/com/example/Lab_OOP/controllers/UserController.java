package com.example.Lab_OOP.controllers;

import com.example.Lab_OOP.models.Role;
import com.example.Lab_OOP.models.User;
import com.example.Lab_OOP.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/users")
@PreAuthorize("hasRole('ADMIN')")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public String listUsers(Model model) {
        model.addAttribute("users", userRepository.findAll());
        return "users";
    }

    @GetMapping("/add")
    public String addUserForm(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("roles", Role.values());
        return "user-form";
    }

    @PostMapping("/add")
    public String addUser(@ModelAttribute User user) {
        // пароли хранятся в зашифрованном виде — здесь должен быть encoder
        userRepository.save(user);
        return "redirect:/users";
    }

    @GetMapping("/{id}/edit")
    public String editUser(@PathVariable int id, Model model) {
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()) return "redirect:/users";
        model.addAttribute("user", user.get());
        model.addAttribute("roles", Role.values());
        return "user-form";
    }

    @PostMapping("/{id}/edit")
    public String updateUser(@PathVariable int id, @ModelAttribute User user) {
        user.setId(id);
        userRepository.save(user);
        return "redirect:/users";
    }

    @PostMapping("/{id}/delete")
    public String deleteUser(@PathVariable int id) {
        userRepository.deleteById(id);
        return "redirect:/users";
    }
}
