package com.mnogo.transactions.service;

import com.mnogo.transactions.dto.request.SignInRequest;
import com.mnogo.transactions.dto.request.SignUpRequest;
import com.mnogo.transactions.entity.User;
import com.mnogo.transactions.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User signUp(SignUpRequest signUpRequest) {
        User user = User.builder()
                .username(signUpRequest.getUsername())
                .password(passwordEncoder.encode(signUpRequest.getPassword()))
                .email(signUpRequest.getEmail())
                .phone(signUpRequest.getPhone())
                .gender(signUpRequest.getGender())
                .avatarPath(signUpRequest.getAvatarPath())
                .build();

        return userRepository.save(user);
    }

    @Override
    public void validateCredentials(SignInRequest signInRequest) {
        Optional<User> optionalUser = userRepository.findByUsername(signInRequest.getUsername());

        if (optionalUser.isEmpty() ||
                !passwordEncoder.matches(signInRequest.getPassword(), optionalUser.get().getPassword())) {
            throw new BadCredentialsException("Неверный логин или пароль");
        }
    }
}
