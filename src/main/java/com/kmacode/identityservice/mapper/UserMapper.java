package com.kmacode.identityservice.mapper;

import com.kmacode.identityservice.dto.request.UserCreationRequest;
import com.kmacode.identityservice.dto.request.UserUpdateRequest;
import com.kmacode.identityservice.dto.response.UserResponse;
import com.kmacode.identityservice.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(UserCreationRequest userCreationRequest);

    UserResponse toUserResponse(User user);

    void updateUser(@MappingTarget User user, UserUpdateRequest userUpdateRequest);

}
