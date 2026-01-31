package com.URLShortener.URLShortener.services;

import com.URLShortener.URLShortener.domain.dto.RegisterRequestDto;
import com.URLShortener.URLShortener.domain.entities.User;

public interface UserService {
    User createUser(RegisterRequestDto registerRequestDto);

    User saveUser(User user);

    void updatePassword(User user, String newPassword);
}
