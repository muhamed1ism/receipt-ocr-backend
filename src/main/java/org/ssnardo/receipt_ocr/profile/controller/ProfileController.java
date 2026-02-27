package org.ssnardo.receipt_ocr.profile.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.ssnardo.receipt_ocr.profile.dto.ProfileCreate;
import org.ssnardo.receipt_ocr.profile.dto.ProfileResponse;
import org.ssnardo.receipt_ocr.profile.dto.ProfileUpdate;
import org.ssnardo.receipt_ocr.profile.entity.Profile;
import org.ssnardo.receipt_ocr.profile.mapper.ProfileMapper;
import org.ssnardo.receipt_ocr.profile.service.ProfileService;
import org.ssnardo.receipt_ocr.user.mapper.UserMapper;
import org.ssnardo.receipt_ocr.user.dto.UserResponse;
import org.ssnardo.receipt_ocr.user.service.UserService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/profiles")
public class ProfileController {

    private final ProfileService profileService;
    private final UserService userService;
    private final ProfileMapper profileMapper;
    private final UserMapper userMapper;

    @GetMapping()
    ResponseEntity<List<ProfileResponse>> getAllProfiles() {

        return ResponseEntity.ok(profileService.getAllProfiles());
    }

    @GetMapping("/me")
    ResponseEntity<ProfileResponse> getProfileMe() {

        UserResponse currentUser = userMapper.toResponseDto(userService.getCurrentUser());
        Profile profile = profileService.getProfileByUserId(currentUser.getId());
        return ResponseEntity.ok(profileMapper.toResponseDto(profile));
    }

    @GetMapping("/{user_id}")
    ResponseEntity<ProfileResponse> getProfileByUserId(@PathVariable("user_id") UUID user_id) {

        Profile profile = profileService.getProfileByUserId(user_id);
        return ResponseEntity.ok(profileMapper.toResponseDto(profile));
    }

    @PostMapping("/me")
    ResponseEntity<ProfileResponse> createProfileMe(@Valid @RequestBody ProfileCreate dto) {

        return ResponseEntity.status(HttpStatus.CREATED).body(profileService.createProfileMe(dto));
    }

    @PostMapping("/{user_id}")
    ResponseEntity<ProfileResponse> createProfile(@PathVariable("user_id") UUID user_id,
            @Valid @RequestBody ProfileCreate dto) {

        return ResponseEntity.status(HttpStatus.CREATED).body(profileService.createProfile(dto, user_id));
    }

    @PatchMapping("/me")
    ResponseEntity<ProfileResponse> updateProfileMe(@Valid @RequestBody ProfileUpdate dto) {

        return ResponseEntity.ok(profileService.updateProfileMe(dto));
    }

    @PatchMapping("/{user_id}")
    ResponseEntity<ProfileResponse> updateProfile(@PathVariable("user_id") UUID user_id,
            @Valid @RequestBody ProfileUpdate dto) {

        return ResponseEntity.ok(profileService.updateProfile(dto, user_id));
    }
}
