package com.learn.authentification.service;

import java.util.Date;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import it.antonio.sp.entity.AuthEntity;
import it.antonio.sp.entity.UserEntity;
import it.antonio.sp.repository.AuthRepository;
import it.antonio.sp.repository.UserRepository;
import it.antonio.sp.service.AuthService;
import it.antonio.sp.util.Generator;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AuthServiceTest {

    @Autowired
    AuthService service;
    
    @Autowired
    UserRepository userRepository;
    
    @Autowired
    AuthRepository authRepository;

    UserEntity user;
    AuthEntity auth;

    @BeforeAll
    void setUp() {
        user = new UserEntity();
        user.setCreatedAt(new Date());
        user.setEmail("tes@email.com");
        user.setPassword(Generator
                .generatePassword("passwordtesbro"));
        user.setId("tesId");
        userRepository.save(user).block();
    }

    @AfterAll
    void clear() {
        userRepository.delete(user).block();
        authRepository.delete(
                authRepository
                        .findById(user.getId())
                        .block()).block();
    }
}
