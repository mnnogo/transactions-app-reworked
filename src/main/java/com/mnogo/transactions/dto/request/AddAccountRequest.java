package com.mnogo.transactions.dto.request;

import com.mnogo.transactions.enums.AccountType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AddAccountRequest {

    @NotNull
    private Long userId;

    @NotBlank
    private String accountName;

    @NotNull
    private AccountType accountType;
}
