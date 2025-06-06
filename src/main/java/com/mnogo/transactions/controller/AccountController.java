package com.mnogo.transactions.controller;

import com.mnogo.transactions.dto.request.AddAccountRequest;
import com.mnogo.transactions.dto.response.AccountResponse;
import com.mnogo.transactions.entity.Account;
import com.mnogo.transactions.service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
@Tag(name = "Счета")
public class AccountController {

    private final AccountService accountService;

    @GetMapping("/{userId}")
    @Operation(summary = "Получить список счетов пользователя")
    public List<AccountResponse> getUserAccounts(@PathVariable long userId) {
        List<Account> accountList = accountService.findAllAccountsByUserId(userId);

        return accountList.stream()
                .map(AccountResponse::new)
                .toList();
    }

    @PostMapping
    @Operation(summary = "Добавить новый счёт")
    public AccountResponse addNewAccount(@RequestBody @Valid AddAccountRequest addAccountRequest) {
        Account newAccount = accountService.saveAccount(addAccountRequest);
        return new AccountResponse(newAccount);
    }
}
