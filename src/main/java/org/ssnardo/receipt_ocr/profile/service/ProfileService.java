package org.ssnardo.receipt_ocr.profile.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.ssnardo.receipt_ocr.common.exception.ResourceNotFoundException;
import org.ssnardo.receipt_ocr.profile.dto.ProfileCreate;
import org.ssnardo.receipt_ocr.profile.dto.ProfileResponse;
import org.ssnardo.receipt_ocr.profile.dto.ProfileUpdate;
import org.ssnardo.receipt_ocr.profile.entity.Profile;
import org.ssnardo.receipt_ocr.profile.mapper.ProfileMapper;
import org.ssnardo.receipt_ocr.profile.repository.ProfileRepository;
import org.ssnardo.receipt_ocr.user.entity.User;
import org.ssnardo.receipt_ocr.user.repository.UserRepository;
import org.ssnardo.receipt_ocr.user.service.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProfileService {

    public final ProfileRepository profileRepository;
    public final UserRepository userRepository;
    public final UserService userService;
    public final ProfileMapper profileMapper;

    public List<ProfileResponse> getAllProfiles() {

        return profileRepository.findAll()
                .stream()
                .map(profileMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    public Profile getProfileByUserId(UUID user_id) {

        Optional<Profile> optionalProfile = profileRepository.findByUserId(user_id);
        if (!optionalProfile.isPresent()) {
            throw new ResourceNotFoundException(
                    "Profile with user ID " + user_id + " not found");
        }
        return optionalProfile.get();
    }

    public ProfileResponse createProfile(ProfileCreate dto, UUID user_id) {

        Optional<User> optionalUser = userRepository.findById(user_id);
        if (!optionalUser.isPresent()) {
            throw new ResourceNotFoundException(
                    "User with ID " + user_id + " not found");
        }
        Profile profile = profileMapper.toEntity(dto, optionalUser.get());
        return profileMapper.toResponseDto(
                profileRepository.save(profile));
    }

    public ProfileResponse createProfileMe(ProfileCreate dto) {

        User currentUser = userService.getCurrentUser();
        Profile profile = profileMapper.toEntity(dto, currentUser);
        return profileMapper.toResponseDto(
                profileRepository.save(profile));
    }

    public ProfileResponse updateProfile(ProfileUpdate dto, UUID user_id) {

        Profile profile = getProfileByUserId(user_id);
        profileMapper.updateEntityFromDto(dto, profile);
        return profileMapper.toResponseDto(
                profileRepository.save(profile));
    }

    public ProfileResponse updateProfileMe(ProfileUpdate dto) {

        User currentUser = userService.getCurrentUser();
        return updateProfile(dto, currentUser.getId());
    }
}
