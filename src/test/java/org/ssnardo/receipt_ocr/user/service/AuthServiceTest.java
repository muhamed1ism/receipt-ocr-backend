package org.ssnardo.receipt_ocr.user.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.ssnardo.receipt_ocr.common.exception.ResourceAlreadyExistsException;
import org.ssnardo.receipt_ocr.user.User;
import org.ssnardo.receipt_ocr.user.UserMapper;
import org.ssnardo.receipt_ocr.user.UserRepository;
import org.ssnardo.receipt_ocr.user.dto.LoginRequest;
import org.ssnardo.receipt_ocr.user.dto.UserRegister;
import org.ssnardo.receipt_ocr.user.dto.UserResponse;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private AuthService authService;

    @Test
    void givenValidUserData_whenRegister_thenSaveUser() {
        UserRegister dto = new UserRegister("test@example.com", "password123");
        User user = new User();
        UserResponse response = new UserResponse();

        when(userRepository.existsByEmail(dto.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(dto.getPassword())).thenReturn("hashedPassword");
        when(userMapper.toEntity(dto)).thenReturn(user);
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.toResponseDto(user)).thenReturn(response);

        UserResponse result = authService.registerUser(dto);

        assertThat(result).isEqualTo(response);
        verify(userRepository).save(user);
        verify(passwordEncoder).encode(dto.getPassword());
        verify(userMapper).toEntity(dto);
    }

    @Test
    void givenExistingEmail_whenRegister_thenThrowException() {
        UserRegister dto = new UserRegister("test@test.com", "password123");

        when(userRepository.existsByEmail(dto.getEmail())).thenReturn(true);

        assertThatThrownBy(() -> authService.registerUser(dto))
                .isInstanceOf(ResourceAlreadyExistsException.class)
                .hasMessageContaining("already exists");
    }

    @Test
    void givenCorrectPassword_whenComparePassword_thenReturnTrue() {
        LoginRequest request = new LoginRequest();
        request.setEmail("test@example.com");
        request.setPassword("plainPassword");

        User user = new User();
        user.setHashedPassword("hashedPassword");

        when(userRepository.findByEmail("test@example.com"))
                .thenReturn(Optional.of(user));
        when(passwordEncoder.matches("plainPassword", "hashedPassword"))
                .thenReturn(true);

        boolean result = authService.comparePassword(request);

        assertThat(result).isTrue();
    }

    @Test
    void givenIncorrectPassword_whenComparePassword_thenReturnFalse() {
        LoginRequest request = new LoginRequest();
        request.setEmail("test@example.com");
        request.setPassword("wrongPassword");

        User user = new User();
        user.setHashedPassword("hashedPassword");

        when(userRepository.findByEmail("test@example.com"))
                .thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrongPassword", "hashedPassword"))
                .thenReturn(false);

        boolean result = authService.comparePassword(request);

        assertThat(result).isFalse();
    }
}
