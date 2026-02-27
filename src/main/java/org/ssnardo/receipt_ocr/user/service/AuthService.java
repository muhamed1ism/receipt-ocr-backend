package org.ssnardo.receipt_ocr.user.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.ssnardo.receipt_ocr.common.exception.ResourceAlreadyExistsException;
import org.ssnardo.receipt_ocr.common.exception.ResourceNotFoundException;
import org.ssnardo.receipt_ocr.user.entity.User;
import org.ssnardo.receipt_ocr.user.mapper.UserMapper;
import org.ssnardo.receipt_ocr.user.repository.UserRepository;
import org.ssnardo.receipt_ocr.user.dto.LoginRequest;
import org.ssnardo.receipt_ocr.user.dto.UserRegister;
import org.ssnardo.receipt_ocr.user.dto.UserResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public boolean comparePassword(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(
                        () -> new ResourceNotFoundException("User with email " + request.getEmail() + " not found"));

        return passwordEncoder.matches(request.getPassword(), user.getHashedPassword());
    }

    public UserResponse registerUser(UserRegister dto) {

        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new ResourceAlreadyExistsException("Email already exists");
        }

        String hashedPassword = passwordEncoder.encode(dto.getPassword());

        User user = userMapper.toEntity(dto);
        user.setHashedPassword(hashedPassword);

        return userMapper.toResponseDto(userRepository.save(user));
    }
}
