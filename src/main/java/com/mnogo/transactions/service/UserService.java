package com.mnogo.transactions.service;

import com.mnogo.transactions.dto.response.TransactionResponse;
import com.mnogo.transactions.entity.Transaction;
import com.mnogo.transactions.entity.User;

import java.util.List;

public interface UserService {
    User findById(long id);
}
