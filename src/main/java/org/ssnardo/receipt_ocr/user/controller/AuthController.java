package org.ssnardo.receipt_ocr.user.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.ssnardo.receipt_ocr.security.util.JwtUtil;
import org.ssnardo.receipt_ocr.user.dto.LoginRequest;
import org.ssnardo.receipt_ocr.user.dto.UserRegister;
import org.ssnardo.receipt_ocr.user.dto.UserResponse;
import org.ssnardo.receipt_ocr.user.service.AuthService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

  private final JwtUtil jwtUtil;
  private final AuthService authService;

  @PostMapping("/login")
  public ResponseEntity<?> login(@RequestBody LoginRequest request) {
    if (!"password".equals(request.getPassword())) {
      return ResponseEntity.status(401).body("Invalid credentials");
    }

    String token = jwtUtil.generateToken(request.getEmail());
    return ResponseEntity.ok(new JwtResponse(token));
  }

  @PostMapping("/register")
  ResponseEntity<UserResponse> registerUser(@Valid @RequestBody UserRegister registerDto) {
    return ResponseEntity.status(HttpStatus.CREATED).body(authService.registerUser(registerDto));
  }
}

record JwtResponse(String token) {
}
