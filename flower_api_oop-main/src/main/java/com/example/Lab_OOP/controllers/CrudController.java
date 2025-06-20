package com.example.Lab_OOP.controllers;

import com.example.Lab_OOP.models.Auto;
import com.example.Lab_OOP.repo.AutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Optional;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

@Controller
public class CrudController {

    @Autowired
    private AutoRepository autoRepository;

    @GetMapping("/whoami")
    @ResponseBody
    public String whoAmI(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated() ||
                authentication.getPrincipal().equals("anonymousUser")) {
            return "Пользователь не аутентифицирован.";
        }

        String username = authentication.getName();
        StringBuilder roles = new StringBuilder();

        for (GrantedAuthority authority : authentication.getAuthorities()) {
            roles.append(authority.getAuthority()).append(" ");
        }

        return "Пользователь: " + username + "<br>Роли: " + roles;
    }

    @PreAuthorize("hasAnyRole('USER', 'MODERATOR', 'ADMIN')")
    @GetMapping("/crud")
    public String crud(Model model) {
        Iterable<Auto> autos = autoRepository.findAll();
        autos.forEach(a -> System.out.println(a.getMark() + " " + a.getModel()));
        model.addAttribute("autos", autos);
        return "crud";
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR')")
    @GetMapping("/crud/add")
    public String crudAdd(Model model) {
        return "crud-new";
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR')")
    @PostMapping("/crud/add")
    public String crudAutoAdd(@RequestParam String mark,
                              @RequestParam String carModel,
                              @RequestParam Integer mileage,
                              Model modelView) {
        Auto auto = new Auto(mark, carModel, mileage);
        autoRepository.save(auto);
        return "redirect:/crud";
    }

    @PreAuthorize("hasAnyRole('USER', 'MODERATOR', 'ADMIN')")
    @GetMapping("/crud/{id}")
    public String crudDetails(@PathVariable(value = "id") int id, Model model) {
        if (!autoRepository.existsById(id)) {
            return "redirect:/crud";
        }
        Optional<Auto> auto = autoRepository.findById(id);
        ArrayList<Auto> res = new ArrayList<>();
        auto.ifPresent(res::add);
        model.addAttribute("auto", res);
        return "crud-details";
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR')")
    @GetMapping("/crud/{id}/edit")
    public String crudEdit(@PathVariable(value = "id") int id, Model model) {
        if (!autoRepository.existsById(id)) {
            return "redirect:/crud";
        }
        Optional<Auto> auto = autoRepository.findById(id);
        ArrayList<Auto> res = new ArrayList<>();
        auto.ifPresent(res::add);
        model.addAttribute("auto", res);
        return "crud-edit";
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR')")
    @PostMapping("/crud/{id}/edit")
    public String crudAutoEdit(@PathVariable("id") Integer carId,
                               @RequestParam("mark") String mark,
                               @RequestParam("model") String model,
                               @RequestParam("mileage") Integer mileage,
                               Model uiModel) {
        Auto auto = autoRepository.findById(carId).orElseThrow();
        auto.setMark(mark);
        auto.setModel(model);
        auto.setMileage(mileage);
        autoRepository.save(auto);
        return "redirect:/crud";
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR')")
    @PostMapping("/crud/{id}/remove")
    public String crudAutoDelete(@PathVariable Integer id, Model modelView) {
        Auto auto = autoRepository.findById(id).orElseThrow();
        autoRepository.delete(auto);
        return "redirect:/crud";
    }
}