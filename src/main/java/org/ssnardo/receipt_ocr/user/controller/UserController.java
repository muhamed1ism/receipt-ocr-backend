package org.ssnardo.receipt_ocr.user.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.ssnardo.receipt_ocr.user.mapper.UserMapper;
import org.ssnardo.receipt_ocr.user.dto.UserCreate;
import org.ssnardo.receipt_ocr.user.dto.UserResponse;
import org.ssnardo.receipt_ocr.user.dto.UserUpdate;
import org.ssnardo.receipt_ocr.user.dto.UserUpdateMe;
import org.ssnardo.receipt_ocr.user.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    @GetMapping()
    ResponseEntity<List<UserResponse>> getAllUsers() {

        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/me")
    ResponseEntity<UserResponse> getCurrentUser() {

        UserResponse currentUser = userMapper.toResponseDto(userService.getCurrentUser());
        return ResponseEntity.ok(currentUser);
    }

    @GetMapping("/{id}")
    // @Operation(summary = "Get a user by ID", description = "Returns a single user
    // by their UUID")
    ResponseEntity<UserResponse> getUserById(@PathVariable("id") UUID id) {

        UserResponse user = userMapper.toResponseDto(userService.getUserById(id));
        return ResponseEntity.ok(user);
    }

    @PostMapping()
    ResponseEntity<UserResponse> createUser(@Valid @RequestBody UserCreate dto) {

        return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(dto));
    }

    @PatchMapping("/me")
    ResponseEntity<UserResponse> updateUserMe(@Valid @RequestBody UserUpdateMe dto) {

        return ResponseEntity.ok(userService.updateUserMe(dto));
    }

    @PatchMapping("/{id}")
    ResponseEntity<UserResponse> updateUser(@PathVariable("id") UUID id, @Valid @RequestBody UserUpdate dto) {

        return ResponseEntity.ok(userService.updateUser(dto, id));
    }

    @DeleteMapping("/{id}")
    ResponseEntity<Void> deleteUser(@PathVariable("id") UUID id) {

        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
