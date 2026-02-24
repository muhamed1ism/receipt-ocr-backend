package org.ssnardo.receipt_ocr.user.service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.ssnardo.receipt_ocr.common.exception.ResourceAlreadyExistsException;
import org.ssnardo.receipt_ocr.common.exception.ResourceNotFoundException;
import org.ssnardo.receipt_ocr.common.exception.UnauthenticatedException;
import org.ssnardo.receipt_ocr.security.CustomUserDetails;
import org.ssnardo.receipt_ocr.user.User;
import org.ssnardo.receipt_ocr.user.dto.UserCreate;
import org.ssnardo.receipt_ocr.user.dto.UserResponse;
import org.ssnardo.receipt_ocr.user.dto.UserUpdate;
import org.ssnardo.receipt_ocr.user.dto.UserUpdateMe;
import org.ssnardo.receipt_ocr.user.mapper.UserMapper;
import org.ssnardo.receipt_ocr.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;
  private final UserMapper userMapper;
  private final PasswordEncoder passwordEncoder;

  public List<UserResponse> getAllUsers() {

    return userRepository.findAll()
        .stream()
        .map(userMapper::toResponseDto)
        .collect(Collectors.toList());
  }

  public UserResponse getUserById(UUID id) {

    return userRepository.findById(id)
        .map(userMapper::toResponseDto)
        .orElseThrow(() -> new ResourceNotFoundException("User with ID " + id + " not found"));
  }

  public User getCurrentUser() {

    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    if (auth == null || !(auth.getPrincipal() instanceof CustomUserDetails)) {
      throw new UnauthenticatedException("User not authenticated");
    }
    CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();
    return userDetails.getUser();
  }

  public UserResponse createUser(UserCreate dto) {

    if (userRepository.existsByEmail(dto.getEmail())) {
      throw new ResourceAlreadyExistsException("Email already exists");
    }
    String hashedPassword = passwordEncoder.encode(dto.getPassword());
    User user = userMapper.toEntity(dto);
    user.setHashedPassword(hashedPassword);
    return userMapper.toResponseDto(userRepository.save(user));
  }

  @Transactional
  public UserResponse updateUser(UserUpdate updateDto, UUID id) {

    User user = userRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("User with ID " + id + " not found"));
    userMapper.updateEntityFromDto(updateDto, user);
    return userMapper.toResponseDto(userRepository.save(user));
  }

  public UserResponse updateUserMe(UserUpdateMe updateMeDto) {

    User user = getCurrentUser();
    userMapper.updateEntityFromUpdateMeDto(updateMeDto, user);
    return userMapper.toResponseDto(userRepository.save(user));
  }

  public void deleteUser(UUID id) {
    User user = userRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("User with ID " + id + " not found"));
    userRepository.delete(user);
  }
}
