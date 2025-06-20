package com.example.Lab_OOP.controllers;

import com.example.Lab_OOP.models.Auto;
import com.example.Lab_OOP.repo.AutoRepository;
import com.example.Lab_OOP.repo.UserRepository;
import com.example.Lab_OOP.security.SecurityConfig;
import com.example.Lab_OOP.security.jwt.JwtFilter;
import com.example.Lab_OOP.security.jwt.JwtUtil;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.thymeleaf.templateresolver.ITemplateResolver;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = CrudController.class)
@Import({SecurityConfig.class, CrudControllerTest.ThymeleafConfig.class})
@AutoConfigureMockMvc(addFilters = false)
@EnableMethodSecurity
public class CrudControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AutoRepository autoRepository;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private JwtFilter jwtFilter;

    @MockBean
    private JwtUtil jwtUtil;

    @TestConfiguration
    static class ThymeleafConfig {
        @Bean
        public ITemplateResolver templateResolver() {
            ClassLoaderTemplateResolver resolver = new ClassLoaderTemplateResolver();
            resolver.setPrefix("templates/");
            resolver.setSuffix(".html");
            resolver.setTemplateMode("HTML");
            resolver.setCharacterEncoding("UTF-8");
            return resolver;
        }
    }

    @Test
    @WithMockUser(roles = "USER")
    void testCrudAccessWithUserRole() throws Exception {
        List<Auto> autos = List.of(new Auto("Toyota", "Camry", 50000));
        Mockito.when(autoRepository.findAll()).thenReturn(autos);

        mockMvc.perform(get("/crud"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("autos"))  // Исправлено с "flowers" на "autos"
                .andExpect(view().name("crud"));
    }

    @Test
    @WithMockUser(roles = "MODERATOR")
    void testEditAutoFormSuccess() throws Exception {
        Auto auto = new Auto("Toyota", "Camry", 50000);  // Исправлены параметры конструктора
        auto.setId(1);

        Mockito.when(autoRepository.existsById(1)).thenReturn(true);
        Mockito.when(autoRepository.findById(1)).thenReturn(Optional.of(auto));

        mockMvc.perform(get("/crud/1/edit"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("auto"))  // Исправлено с "flower" на "auto"
                .andExpect(view().name("crud-edit"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testSubmitEditAutoForm() throws Exception {
        Auto auto = new Auto("Toyota", "Camry", 50000);  // Исправлены параметры конструктора
        auto.setId(1);

        Mockito.when(autoRepository.findById(1)).thenReturn(Optional.of(auto));
        Mockito.when(autoRepository.save(any(Auto.class))).thenReturn(auto);

        mockMvc.perform(post("/crud/1/edit")
                        .with(csrf())
                        .param("mark", "Toyota")      // Исправлены параметры запроса
                        .param("model", "Corolla")    // Исправлены параметры запроса
                        .param("mileage", "60000"))   // Исправлены параметры запроса
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/crud"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testDeleteAuto() throws Exception {
        Auto auto = new Auto("Toyota", "Camry", 50000);  // Исправлены параметры конструктора
        auto.setId(1);

        Mockito.when(autoRepository.findById(1)).thenReturn(Optional.of(auto));
        Mockito.doNothing().when(autoRepository).delete(any(Auto.class));

        mockMvc.perform(post("/crud/1/remove")
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/crud"));
    }
}