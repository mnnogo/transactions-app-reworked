package com.mnogo.transactions.controller;

import com.mnogo.transactions.dto.request.DepositRequest;
import com.mnogo.transactions.dto.request.TransferRequest;
import com.mnogo.transactions.dto.request.WithdrawRequest;
import com.mnogo.transactions.dto.response.TransactionResponse;
import com.mnogo.transactions.entity.Transaction;
import com.mnogo.transactions.service.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
@Tag(name = "Транзакции")
public class TransactionController {

    private final TransactionService transactionService;

    @GetMapping("/{userId}")
    @Operation(summary = "Получить все транзакции пользователя по всем счетам единым списком")
    public List<TransactionResponse> getUserTransactions(@PathVariable long userId) {
        List<Transaction> transactionList = transactionService.getTransactionsByUserId(userId);

        return transactionList.stream().map(TransactionResponse::new).toList();
    }

    @PostMapping("/transfer")
    @Operation(summary = "Перевод денег между двумя счетами")
    public TransactionResponse transfer(@RequestBody @Valid TransferRequest transferRequest) {
        Transaction resultingTransaction = transactionService.transfer(transferRequest);

        return new TransactionResponse(resultingTransaction);
    }

    @PostMapping("/deposit")
    @Operation(summary = "Пополнение счета")
    public TransactionResponse deposit(@RequestBody @Valid DepositRequest depositRequest) {
        Transaction resultingTransaction = transactionService.deposit(depositRequest);

        return new TransactionResponse(resultingTransaction);
    }

    @PostMapping("/withdraw")
    @Operation(summary = "Снятие денег с счета")
    public TransactionResponse withdraw(@RequestBody @Valid WithdrawRequest withdrawRequest) {
        Transaction resultingTransaction = transactionService.withdraw(withdrawRequest);

        return new TransactionResponse(resultingTransaction);
    }
}
