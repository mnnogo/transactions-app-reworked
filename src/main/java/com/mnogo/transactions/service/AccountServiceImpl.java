package com.mnogo.transactions.service;

import com.mnogo.transactions.dto.request.AddAccountRequest;
import com.mnogo.transactions.entity.Account;
import com.mnogo.transactions.entity.User;
import com.mnogo.transactions.exception.UserNotFoundException;
import com.mnogo.transactions.repository.AccountRepository;
import com.mnogo.transactions.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;

    @Override
    public Account saveAccount(AddAccountRequest addAccountRequest) {
        if (!userRepository.existsById(addAccountRequest.getUserId())) {
            throw new UserNotFoundException("Пользователя с ID = " + addAccountRequest.getUserId() + " не существует");
        }

        Account account = Account.builder()
                .name(addAccountRequest.getAccountName())
                .owner(User.builder().id(addAccountRequest.getUserId()).build())  // пользователь-заглушка, нужен только id
                .type(addAccountRequest.getAccountType())
                .balance(0)
                .income(0)
                .expense(0)
                .build();

        return accountRepository.save(account);
    }

    @Override
    public List<Account> findAllAccountsByUserId(long userId) {
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException("Пользователя с ID = " + userId + " не существует");
        }

        return accountRepository.findAllByOwner_Id(userId);
    }
}
