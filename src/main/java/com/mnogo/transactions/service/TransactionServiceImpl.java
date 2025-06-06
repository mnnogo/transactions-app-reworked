package com.mnogo.transactions.service;

import com.mnogo.transactions.dto.request.DepositRequest;
import com.mnogo.transactions.dto.request.TransferRequest;
import com.mnogo.transactions.dto.request.WithdrawRequest;
import com.mnogo.transactions.entity.Account;
import com.mnogo.transactions.entity.Transaction;
import com.mnogo.transactions.enums.AccountType;
import com.mnogo.transactions.exception.*;
import com.mnogo.transactions.repository.AccountRepository;
import com.mnogo.transactions.repository.TransactionRepository;
import com.mnogo.transactions.repository.UserRepository;
import jakarta.annotation.Nullable;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;

    @Value("${account.credit.minimum_balance}")
    private float MINIMUM_CREDIT_BALANCE;
    @Value("${account.credit.minimum_not_problematic_balance}")
    private float MINIMUM_NOT_PROBLEMATIC_CREDIT_BALANCE;
    @Value("${transaction.debit.minimum_deposit_for_bonus_gain}")
    private float MINIMUM_DEPOSIT_FOR_BONUS_GAIN;
    @Value("${transaction.debit.deposit_bonus}")
    private float DEPOSIT_BONUS;
    @Value("${transaction.max_sum_per_withdraw}")
    private float MAX_SUM_PER_WITHDRAW;

    @Override
    public List<Transaction> getTransactionsByUserId(long userId) {
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException(String.format("Пользователя с ID = %d не существует", userId));
        }

        return transactionRepository.findAllTransactionsByUserId(userId);
    }

    @Override
    @Transactional
    public Transaction transfer(TransferRequest transferRequest) {
        Optional<Account> fromAccountOptional = accountRepository.findById(transferRequest.getFromAccountId());
        Optional<Account> toAccountOptional = accountRepository.findById(transferRequest.getToAccountId());

        if (fromAccountOptional.isEmpty()) {
            throw new AccountNotFoundException("Счета с ID = " + transferRequest.getFromAccountId() + " не существует");
        }

        if (toAccountOptional.isEmpty()) {
            throw new AccountNotFoundException("Счета с ID = " + transferRequest.getToAccountId() + " не существует");
        }

        Account fromAccount = fromAccountOptional.get();
        Account toAccount = toAccountOptional.get();

        if (fromAccount.getType() == AccountType.DEBIT && (fromAccount.getBalance() - transferRequest.getSum()) < 0) {
            throw new NotEnoughMoneyException("Недостаточно средств на дебетовом счёте");
        }

        if (fromAccount.getType() == AccountType.CREDIT && (fromAccount.getBalance() - transferRequest.getSum()) < MINIMUM_CREDIT_BALANCE) {
            throw new NotEnoughMoneyException(
                    String.format("Превышен лимит кредитного счёта (%f₽)", MINIMUM_CREDIT_BALANCE)
            );
        }

        // если перевод с дебетового счета НЕ на кредитный, то проверяем бизнес-условия
        if (fromAccount.getType() == AccountType.DEBIT && toAccount.getType() != AccountType.CREDIT) {

            if (accountRepository.existsCreditAccountWithLowBalance(
                    fromAccount.getOwner().getId(),
                    MINIMUM_NOT_PROBLEMATIC_CREDIT_BALANCE)
            ) {
                throw new BadCreditStateException(
                        String.format("Операция запрещена: у вас есть кредитный счёт с задолженностью более %f₽",
                                MINIMUM_NOT_PROBLEMATIC_CREDIT_BALANCE)
                );
            }
        }

        return performAccountTransaction(fromAccount, toAccount, transferRequest.getSum());
    }

    @Override
    @Transactional
    public Transaction deposit(DepositRequest depositRequest) {
        Optional<Account> toAccountOptional = accountRepository.findById(depositRequest.getToAccountId());

        if (toAccountOptional.isEmpty()) {
            throw new AccountNotFoundException("Счета с ID = " + depositRequest.getToAccountId() + " не существует");
        }

        Account toAccount = toAccountOptional.get();

        if (toAccount.getType() != AccountType.CREDIT &&
                accountRepository.existsCreditAccountWithLowBalance(
                        toAccount.getOwner().getId(),
                        MINIMUM_NOT_PROBLEMATIC_CREDIT_BALANCE)
        ) {
            throw new BadCreditStateException(
                    String.format("Операция запрещена: у вас есть кредитный счёт с задолженностью более %f₽",
                            MINIMUM_NOT_PROBLEMATIC_CREDIT_BALANCE)
            );
        }

        float bonus = 0;
        if (toAccount.getType() == AccountType.DEBIT && depositRequest.getSum() > MINIMUM_DEPOSIT_FOR_BONUS_GAIN) {
            bonus = DEPOSIT_BONUS;
        }

        return performAccountTransaction(null, toAccount, depositRequest.getSum() + bonus);
    }

    @Override
    @Transactional
    public Transaction withdraw(WithdrawRequest withdrawRequest) {
        if (withdrawRequest.getSum() > MAX_SUM_PER_WITHDRAW) {
            throw new BadRequestException(String.format("Нельзя снимать более %f за одну операцию", MAX_SUM_PER_WITHDRAW));
        }

        Optional<Account> fromAccountOptional = accountRepository.findById(withdrawRequest.getFromAccountId());

        if (fromAccountOptional.isEmpty()) {
            throw new AccountNotFoundException("Счета с ID = " + withdrawRequest.getFromAccountId() + " не существует");
        }

        Account fromAccount = fromAccountOptional.get();

        if (fromAccount.getType() == AccountType.DEBIT && (fromAccount.getBalance() - withdrawRequest.getSum()) < 0) {
            throw new NotEnoughMoneyException("Недостаточно средств на дебетовом счёте");
        }

        if (fromAccount.getType() == AccountType.CREDIT && (fromAccount.getBalance() - withdrawRequest.getSum()) < MINIMUM_CREDIT_BALANCE) {
            throw new NotEnoughMoneyException(
                    String.format("Превышен лимит кредитного счёта (%f₽)", MINIMUM_CREDIT_BALANCE)
            );
        }

        if (fromAccount.getType() != AccountType.CREDIT &&
                accountRepository.existsCreditAccountWithLowBalance(
                        fromAccount.getOwner().getId(),
                        MINIMUM_NOT_PROBLEMATIC_CREDIT_BALANCE)
        ) {
            throw new BadCreditStateException(
                    String.format("Операция запрещена: у вас есть кредитный счёт с задолженностью более %f₽",
                            MINIMUM_NOT_PROBLEMATIC_CREDIT_BALANCE)
            );
        }

        return performAccountTransaction(fromAccount, null, withdrawRequest.getSum());
    }

    private Transaction performAccountTransaction(@Nullable Account fromAccount,
                                                  @Nullable Account toAccount,
                                                  float sum) {

        if (fromAccount == null && toAccount == null) {
            throw new IllegalArgumentException("fromAccount и toAccount оба null");
        }

        Transaction transaction = transactionRepository.save(
                Transaction.builder()
                        .fromAccount(fromAccount)
                        .toAccount(toAccount)
                        .sum(sum)
                        .transactionDateTime(LocalDateTime.now())
                        .build()
        );

        if (fromAccount != null) {
            fromAccount.setBalance(fromAccount.getBalance() - sum);
            fromAccount.setExpense(fromAccount.getExpense() + sum);
            accountRepository.save(fromAccount);  // можно не сохранять, но так более читаемо
        }

        if (toAccount != null) {
            toAccount.setBalance(toAccount.getBalance() + sum);
            toAccount.setIncome(toAccount.getIncome() + sum);
            accountRepository.save(toAccount);
        }

        return transaction;
    }
}
