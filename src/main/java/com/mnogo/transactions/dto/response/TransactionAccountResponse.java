package com.mnogo.transactions.dto.response;

import com.mnogo.transactions.entity.Account;
import com.mnogo.transactions.entity.Transaction;
import com.mnogo.transactions.enums.AccountType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Schema(description = "Вспомогательный класс для отображения аккаунтов в транзакциях")
public class TransactionAccountResponse {
    private long id;
    private String name;
    private AccountType type;
    private float balance;
    private float income;
    private float expense;

    public TransactionAccountResponse(Account account) {
        this.id = account.getId();
        this.name = account.getName();
        this.type = account.getType();
        this.balance = account.getBalance();
        this.income = account.getIncome();
        this.expense = account.getExpense();
    }
}
