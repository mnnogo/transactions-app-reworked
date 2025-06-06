package com.mnogo.transactions.service;

import com.mnogo.transactions.dto.request.AddAccountRequest;
import com.mnogo.transactions.entity.Account;

import java.util.List;

public interface AccountService {
    Account saveAccount(AddAccountRequest account);
    List<Account> findAllAccountsByUserId(long userId);
}
