package org.ssnardo.receipt_ocr.user.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.ssnardo.receipt_ocr.user.User;
import org.ssnardo.receipt_ocr.user.dto.UserCreate;
import org.ssnardo.receipt_ocr.user.dto.UserRegister;
import org.ssnardo.receipt_ocr.user.dto.UserResponse;
import org.ssnardo.receipt_ocr.user.dto.UserUpdate;
import org.ssnardo.receipt_ocr.user.dto.UserUpdateMe;

@Mapper(componentModel = "spring")
public interface UserMapper {

  @Mapping(source = "active", target = "isActive")
  UserResponse toResponseDto(User user);

  @Mapping(ignore = true, target = "id")
  @Mapping(ignore = true, target = "createdAt")
  @Mapping(ignore = true, target = "updatedAt")
  @Mapping(source = "active", target = "isActive")
  @Mapping(source = "password", target = "hashedPassword")
  User toEntity(UserCreate dto);

  @Mapping(ignore = true, target = "id")
  @Mapping(ignore = true, target = "role")
  @Mapping(ignore = true, target = "isActive")
  @Mapping(ignore = true, target = "createdAt")
  @Mapping(ignore = true, target = "updatedAt")
  @Mapping(source = "password", target = "hashedPassword")
  User toEntity(UserRegister dto);

  @Mapping(ignore = true, target = "id")
  @Mapping(ignore = true, target = "createdAt")
  @Mapping(ignore = true, target = "updatedAt")
  @Mapping(source = "isActive", target = "active")
  @Mapping(source = "password", target = "hashedPassword")
  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  void updateEntityFromDto(UserUpdate dto, @MappingTarget User user);

  @Mapping(ignore = true, target = "id")
  @Mapping(ignore = true, target = "role")
  @Mapping(ignore = true, target = "active")
  @Mapping(ignore = true, target = "createdAt")
  @Mapping(ignore = true, target = "updatedAt")
  @Mapping(source = "password", target = "hashedPassword")
  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  void updateEntityFromUpdateMeDto(UserUpdateMe dto, @MappingTarget User user);

}
