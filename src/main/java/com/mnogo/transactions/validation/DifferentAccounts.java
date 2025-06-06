package com.mnogo.transactions.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Constraint(validatedBy = DifferentAccountsValidator.class)
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface DifferentAccounts {
    String message() default "fromAccountId и toAccountId не должны совпадать";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
