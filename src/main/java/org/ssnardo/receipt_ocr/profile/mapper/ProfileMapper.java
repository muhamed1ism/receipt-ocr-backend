package org.ssnardo.receipt_ocr.profile.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.ssnardo.receipt_ocr.profile.dto.ProfileCreate;
import org.ssnardo.receipt_ocr.profile.dto.ProfileResponse;
import org.ssnardo.receipt_ocr.profile.dto.ProfileUpdate;
import org.ssnardo.receipt_ocr.profile.entity.Profile;
import org.ssnardo.receipt_ocr.user.entity.User;

@Mapper(componentModel = "spring")
public interface ProfileMapper {

    ProfileResponse toResponseDto(Profile profile);

    @Mapping(ignore = true, target = "id")
    @Mapping(source = "user", target = "user")
    Profile toEntity(ProfileCreate dto, User user);

    @Mapping(ignore = true, target = "id")
    @Mapping(ignore = true, target = "user")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Profile updateEntityFromDto(ProfileUpdate dto, @MappingTarget Profile profile);
}
