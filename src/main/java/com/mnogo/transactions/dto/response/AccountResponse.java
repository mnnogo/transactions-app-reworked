package com.mnogo.transactions.dto.response;

import com.mnogo.transactions.entity.Account;
import com.mnogo.transactions.entity.User;
import com.mnogo.transactions.enums.AccountType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Schema(description = "Счёт, но без лишней информации")
public class AccountResponse {
    private long id;
    private String name;
    private AccountType type;
    private float balance;
    private float income;
    private float expense;

    public AccountResponse(Account account) {
        this.id = account.getId();
        this.name = account.getName();
        this.type = account.getType();
        this.balance = account.getBalance();
        this.income = account.getIncome();
        this.expense = account.getExpense();
    }
}
