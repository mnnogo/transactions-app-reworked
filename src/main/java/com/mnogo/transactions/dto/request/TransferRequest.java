package com.mnogo.transactions.dto.request;

import com.mnogo.transactions.validation.DifferentAccounts;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@DifferentAccounts  // кастомная валидация
public class TransferRequest {

    @NotNull
    private Long fromAccountId;

    @NotNull
    private Long toAccountId;

    @DecimalMin(value = "0.0", inclusive = false, message = "Сумма перевода должна быть положительным числом")
    @NotNull
    private Float sum;
}
