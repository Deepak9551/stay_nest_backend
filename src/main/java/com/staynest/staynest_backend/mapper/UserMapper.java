package com.staynest.staynest_backend.mapper;

import com.staynest.staynest_backend.dto.SignUpDto;
import com.staynest.staynest_backend.dto.UserDto;
import com.staynest.staynest_backend.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto toDto(User user);
    User toEntity(UserDto userDto);
    User toEntity(SignUpDto signUpDto);
}
