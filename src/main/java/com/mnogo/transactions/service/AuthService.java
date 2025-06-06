package com.mnogo.transactions.service;

import com.mnogo.transactions.dto.request.SignInRequest;
import com.mnogo.transactions.dto.request.SignUpRequest;
import com.mnogo.transactions.entity.User;

public interface AuthService {
    User signUp(SignUpRequest request);
    void validateCredentials(SignInRequest signInRequest);
}
