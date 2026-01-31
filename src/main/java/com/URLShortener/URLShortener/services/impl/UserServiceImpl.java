package com.URLShortener.URLShortener.services.impl;

import com.URLShortener.URLShortener.domain.dto.RegisterRequestDto;
import com.URLShortener.URLShortener.domain.entities.Role;
import com.URLShortener.URLShortener.domain.entities.User;
import com.URLShortener.URLShortener.repositories.jpa.UserRepository;
import com.URLShortener.URLShortener.services.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User createUser(RegisterRequestDto registerRequestDTO) {

        User user = new User();
        LocalDateTime now = LocalDateTime.now();

        user.setEmail(registerRequestDTO.email());
        user.setPassword(passwordEncoder.encode(registerRequestDTO.password()));
        user.setRole(Role.USER);
        user.setVerified(false);
        user.setCreatedAt(now);
        user.setUpdatedAt(now);

        return user;
    }

    public User saveUser(User user) {
        User savedUser = userRepository.save(user);
        return savedUser;
    }

    public void updatePassword(User user, String newPassword) {
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);
    }
}
