package org.ssnardo.receipt_ocr.user.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.*;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.ssnardo.receipt_ocr.common.exception.*;
import org.ssnardo.receipt_ocr.security.CustomUserDetails;
import org.ssnardo.receipt_ocr.user.User;
import org.ssnardo.receipt_ocr.user.dto.*;
import org.ssnardo.receipt_ocr.user.mapper.UserMapper;
import org.ssnardo.receipt_ocr.user.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

  @Mock
  private UserRepository userRepository;

  @Mock
  private UserMapper userMapper;

  @Mock
  private PasswordEncoder passwordEncoder;

  @InjectMocks
  private UserService userService;

  @Test
  void whenFetchAllUsers_thenReturnAll() {
    List<User> users = List.of(new User(), new User());
    when(userRepository.findAll()).thenReturn(users);
    when(userMapper.toResponseDto(any())).thenReturn(new UserResponse());

    List<UserResponse> result = userService.getAllUsers();

    assertThat(result).hasSize(2);
  }

  @Test
  void givenUserId_whenFetchUserById_thenReturnUser() {
    UUID id = UUID.randomUUID();
    User user = new User();

    when(userRepository.findById(id)).thenReturn(Optional.of(user));
    when(userMapper.toResponseDto(user)).thenReturn(new UserResponse());

    UserResponse result = userService.getUserById(id);

    assertThat(result).isNotNull();
  }

  @Test
  void givenNonExistentUserId_whenFetchUserById_thenThrowException() {
    UUID id = UUID.randomUUID();

    when(userRepository.findById(id)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> userService.getUserById(id))
        .isInstanceOf(ResourceNotFoundException.class);
  }

  @Test
  void givenNewUserData_whenCreateUser_thenSaveUser() {
    UserCreate dto = new UserCreate();
    dto.setEmail("test@test.com");
    dto.setPassword("pass");

    User user = new User();
    UserResponse response = new UserResponse();

    when(userRepository.existsByEmail(dto.getEmail())).thenReturn(false);
    when(passwordEncoder.encode(dto.getPassword())).thenReturn("hashed");
    when(userMapper.toEntity(dto)).thenReturn(user);
    when(userRepository.save(user)).thenReturn(user);
    when(userMapper.toResponseDto(user)).thenReturn(response);

    UserResponse result = userService.createUser(dto);

    assertThat(result).isNotNull();
  }

  @Test
  void givenExistingEmail_whenCreateUser_thenThrowException() {
    UserCreate dto = new UserCreate();
    dto.setEmail("test@test.com");

    when(userRepository.existsByEmail(dto.getEmail())).thenReturn(true);

    assertThatThrownBy(() -> userService.createUser(dto))
        .isInstanceOf(ResourceAlreadyExistsException.class);
  }

  @Test
  void givenExistingUserIdAndData_whenUpdateUser_thenUpdateUser() {
    UUID id = UUID.randomUUID();
    User user = new User();
    UserUpdate dto = new UserUpdate();

    when(userRepository.findById(id)).thenReturn(Optional.of(user));
    when(userRepository.save(user)).thenReturn(user);
    when(userMapper.toResponseDto(user)).thenReturn(new UserResponse());

    UserResponse result = userService.updateUser(dto, id);

    assertThat(result).isNotNull();
  }

  @Test
  void givenExistingUserId_whenDeleteUser_thenRemoveUser() {
    UUID id = UUID.randomUUID();
    User user = new User();

    when(userRepository.findById(id)).thenReturn(Optional.of(user));

    userService.deleteUser(id);

    verify(userRepository).delete(user);
  }

  @Test
  void givenAuthenticatedContext_whenGetCurrentUser_thenReturnUser() {
    User user = new User();
    CustomUserDetails details = mock(CustomUserDetails.class);
    when(details.getUser()).thenReturn(user);

    Authentication auth = mock(Authentication.class);
    when(auth.getPrincipal()).thenReturn(details);

    SecurityContextHolder.getContext().setAuthentication(auth);

    User result = userService.getCurrentUser();

    assertThat(result).isEqualTo(user);
  }

  @Test
  void givenNoAuthentication_whenGetCurrentUser_thenThrowException() {
    SecurityContextHolder.clearContext();

    assertThatThrownBy(() -> userService.getCurrentUser())
        .isInstanceOf(UnauthenticatedException.class);
  }
}
