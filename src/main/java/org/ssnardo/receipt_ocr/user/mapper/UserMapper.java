package org.ssnardo.receipt_ocr.user.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.ssnardo.receipt_ocr.profile.entity.Profile;
import org.ssnardo.receipt_ocr.profile.dto.ProfileResponse;
import org.ssnardo.receipt_ocr.user.dto.UserCreate;
import org.ssnardo.receipt_ocr.user.dto.UserRegister;
import org.ssnardo.receipt_ocr.user.dto.UserResponse;
import org.ssnardo.receipt_ocr.user.dto.UserUpdate;
import org.ssnardo.receipt_ocr.user.dto.UserUpdateMe;
import org.ssnardo.receipt_ocr.user.entity.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(source = "active", target = "isActive")
    UserResponse toResponseDto(User user);

    ProfileResponse toResponseDto(Profile profile);

    @Mapping(ignore = true, target = "id")
    @Mapping(ignore = true, target = "createdAt")
    @Mapping(ignore = true, target = "updatedAt")
    @Mapping(ignore = true, target = "profile")
    @Mapping(source = "active", target = "isActive")
    @Mapping(source = "password", target = "hashedPassword")
    User toEntity(UserCreate dto);

    @Mapping(ignore = true, target = "id")
    @Mapping(ignore = true, target = "isActive")
    @Mapping(ignore = true, target = "role")
    @Mapping(ignore = true, target = "createdAt")
    @Mapping(ignore = true, target = "updatedAt")
    @Mapping(ignore = true, target = "profile")
    @Mapping(source = "password", target = "hashedPassword")
    User toEntity(UserRegister dto);

    @Mapping(ignore = true, target = "id")
    @Mapping(ignore = true, target = "createdAt")
    @Mapping(ignore = true, target = "updatedAt")
    @Mapping(ignore = true, target = "profile")
    @Mapping(source = "isActive", target = "active")
    @Mapping(source = "password", target = "hashedPassword")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(UserUpdate dto, @MappingTarget User user);

    @Mapping(ignore = true, target = "id")
    @Mapping(ignore = true, target = "role")
    @Mapping(ignore = true, target = "active")
    @Mapping(ignore = true, target = "createdAt")
    @Mapping(ignore = true, target = "updatedAt")
    @Mapping(ignore = true, target = "profile")
    @Mapping(source = "password", target = "hashedPassword")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromUpdateMeDto(UserUpdateMe dto, @MappingTarget User user);

}
