package org.ssnardo.receipt_ocr.user.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.ssnardo.receipt_ocr.user.User;
import org.ssnardo.receipt_ocr.user.dto.LoginRequest;
import org.ssnardo.receipt_ocr.user.dto.UserRegister;
import org.ssnardo.receipt_ocr.user.dto.UserResponse;
import org.ssnardo.receipt_ocr.user.mapper.UserMapper;
import org.ssnardo.receipt_ocr.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final UserMapper userMapper;

  public boolean comparePassword(LoginRequest request) {
    User user = userRepository.findByEmail(request.getEmail())
        .orElseThrow(() -> new RuntimeException("User not found"));

    if (!user.getHashedPassword().equals(request.getPassword())) {
      return false;
    }

    return true;
  }

  public UserResponse registerUser(UserRegister dto) {

    if (userRepository.existsByEmail(dto.getEmail())) {
      throw new IllegalStateException("User with email already exists");
    }

    String hashedPassword = passwordEncoder.encode(dto.getPassword());

    User user = userMapper.toEntity(dto);
    user.setHashedPassword(hashedPassword);

    return userMapper.toResponseDto(userRepository.save(user));
  }
}
