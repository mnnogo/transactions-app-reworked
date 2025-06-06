package com.mnogo.transactions.controller;

import com.mnogo.transactions.dto.request.SignInRequest;
import com.mnogo.transactions.dto.request.SignUpRequest;
import com.mnogo.transactions.entity.User;
import com.mnogo.transactions.dto.response.UserResponse;
import com.mnogo.transactions.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "Аутентификация")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    @Operation(summary = "Регистрация пользователя")
    public UserResponse signUp(@RequestBody @Valid SignUpRequest signUpRequest) {
        User savedUser = authService.signUp(signUpRequest);

        return new UserResponse(savedUser);
    }

    @PostMapping("/signin")
    @Operation(summary = "Авторизация пользователя")
    public boolean signIn(@RequestBody @Valid SignInRequest signInRequest) {
        authService.validateCredentials(signInRequest);
        return true;
    }
}
