package com.mnogo.transactions.service;

import com.mnogo.transactions.exception.UserNotFoundException;
import com.mnogo.transactions.entity.User;
import com.mnogo.transactions.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public User findById(long id) {
        Optional<User> userOptional = userRepository.findById(id);

        if (userOptional.isEmpty()) {
            throw new UserNotFoundException(String.format("Пользователя с ID = %d не существует", id));
        }

        return userOptional.get();
    }
}
