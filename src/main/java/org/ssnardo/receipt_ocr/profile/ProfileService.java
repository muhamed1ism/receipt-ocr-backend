package org.ssnardo.receipt_ocr.profile;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProfileService {

    public final ProfileRepository profileRepository;

    public Profile getAllProfiles() {
        return new Profile();
    }

    public Optional<Profile> getProfileByUserId(UUID id) {
        return profileRepository.findById(id);
    }
}
