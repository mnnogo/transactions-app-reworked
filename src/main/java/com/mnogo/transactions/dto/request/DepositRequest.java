package com.mnogo.transactions.dto.request;

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
public class DepositRequest {

    @NotNull
    private Long toAccountId;

    @DecimalMin(value = "0.0", inclusive = false, message = "Сумма пополнения должна быть положительным числом")
    @NotNull
    private Float sum;
}
