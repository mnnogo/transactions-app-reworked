package com.mnogo.transactions.controller;

import com.mnogo.transactions.dto.response.UserResponse;
import com.mnogo.transactions.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "Пользователи")
public class UserInfoController {

    private final UserService userService;

    @GetMapping("/users/{id}")
    @Operation(summary = "Получить пользователя")
    public UserResponse getUser(@PathVariable("id") long id) {
        return new UserResponse(userService.findById(id));
    }
}
