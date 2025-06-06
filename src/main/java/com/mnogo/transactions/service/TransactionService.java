package com.mnogo.transactions.service;

import com.mnogo.transactions.dto.request.DepositRequest;
import com.mnogo.transactions.dto.request.TransferRequest;
import com.mnogo.transactions.dto.request.WithdrawRequest;
import com.mnogo.transactions.entity.Transaction;
import jakarta.validation.Valid;

import java.util.List;

public interface TransactionService {
    List<Transaction> getTransactionsByUserId(long id);
    Transaction transfer(TransferRequest transferRequest);
    Transaction deposit(DepositRequest depositRequest);
    Transaction withdraw(WithdrawRequest withdrawRequest);
}
