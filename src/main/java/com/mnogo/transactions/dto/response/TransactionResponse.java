package com.mnogo.transactions.dto.response;

import com.mnogo.transactions.entity.Account;
import com.mnogo.transactions.entity.Transaction;
import com.mnogo.transactions.enums.TransactionType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Schema(description = "Транзакция между двумя аккаунтами (перевод), либо с одним аккаунтом (см. параметры)")
public class TransactionResponse {

    @Schema(description = "Аккаунт, с которого перевели/сняли деньги. Null если было произведено зачисление")
    private TransactionAccountResponse fromAccount;

    @Schema(description = "Аккаунт, на который перевели/зачислили деньги. Null если было произведено снятие")
    private TransactionAccountResponse toAccount;

    private LocalDateTime transactionDate;
    private float sum;
    private TransactionType transactionType;

    public TransactionResponse(Transaction transaction) {
        this.fromAccount = transaction.getFromAccount() != null
                ? new TransactionAccountResponse(transaction.getFromAccount())
                : null;

        this.toAccount = transaction.getToAccount() != null
                ? new TransactionAccountResponse(transaction.getToAccount())
                : null;

        this.transactionDate = transaction.getTransactionDateTime();
        this.sum = transaction.getSum();

        this.transactionType =
                transaction.getFromAccount() == null ? TransactionType.DEPOSIT
                        : transaction.getToAccount() == null ? TransactionType.WITHDRAW
                        : TransactionType.TRANSFER;
    }
}
