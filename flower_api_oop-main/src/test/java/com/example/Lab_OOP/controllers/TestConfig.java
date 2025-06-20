package com.example.Lab_OOP.controllers;

import com.example.Lab_OOP.repo.AutoRepository;
import com.example.Lab_OOP.services.CustomUserDetailsService;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
class TestConfig {

    @Bean
    public AutoRepository flowerRepository() {
        return Mockito.mock(AutoRepository.class);
    }

    @Bean
    public CustomUserDetailsService customUserDetailsService() {
        return Mockito.mock(CustomUserDetailsService.class);
    }
}
