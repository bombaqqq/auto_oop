package com.example.Lab_OOP.controllers;

import com.example.Lab_OOP.models.Role;
import com.example.Lab_OOP.models.User;
import com.example.Lab_OOP.repo.UserRepository;
import com.example.Lab_OOP.security.SecurityConfig;
import com.example.Lab_OOP.security.jwt.JwtUtil;
import com.example.Lab_OOP.services.CustomUserDetailsService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Import(SecurityConfig.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private JwtUtil jwtUtil;

    @MockBean
    private CustomUserDetailsService customUserDetailsService;

    @MockBean
    private org.springframework.security.crypto.password.PasswordEncoder passwordEncoder;

    @Test
    void testLoginSuccess() throws Exception {
        User user = new User();
        user.setLogin("user");
        user.setPassword("pass");
        user.setRole(Role.USER);

        when(userRepository.findByLogin("user")).thenReturn(Optional.of(user));
        when(jwtUtil.generateToken("user", "USER")).thenReturn("mocked-jwt");

        String json = """
            {
              "login": "user",
              "password": "pass"
            }
        """;

        mockMvc.perform(get("/auth/login-web"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"));
    }
}
