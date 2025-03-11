package com.kmacode.identityservice.service;

import com.kmacode.identityservice.dto.request.UserCreationRequest;
import com.kmacode.identityservice.dto.request.UserUpdateRequest;
import com.kmacode.identityservice.dto.response.UserResponse;
import com.kmacode.identityservice.entity.User;

import java.util.List;

public interface UserService {
    UserResponse createUser(UserCreationRequest userCreationRequest);
    UserResponse updateUser(String id, UserUpdateRequest userUpdateRequest);
    UserResponse getUser(String id);
    void deleteUser(String id);
    List<UserResponse> getUsers();
    UserResponse getMyInfo();

}
