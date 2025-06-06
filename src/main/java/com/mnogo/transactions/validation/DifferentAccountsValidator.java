package com.mnogo.transactions.validation;

import com.mnogo.transactions.dto.request.TransferRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Objects;

public class DifferentAccountsValidator implements ConstraintValidator<DifferentAccounts, TransferRequest> {

    @Override
    public boolean isValid(TransferRequest transferRequest, ConstraintValidatorContext constraintValidatorContext) {
        return !(Objects.equals(transferRequest.getFromAccountId(), transferRequest.getToAccountId()));
    }
}
