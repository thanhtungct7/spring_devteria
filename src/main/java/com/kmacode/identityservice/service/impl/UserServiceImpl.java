package com.kmacode.identityservice.service.impl;

import com.kmacode.identityservice.dto.request.UserCreationRequest;
import com.kmacode.identityservice.dto.request.UserUpdateRequest;
import com.kmacode.identityservice.dto.response.UserResponse;
import com.kmacode.identityservice.entity.User;
import com.kmacode.identityservice.enums.Role;
import com.kmacode.identityservice.exception.AppException;
import com.kmacode.identityservice.exception.ErrorCode;
import com.kmacode.identityservice.mapper.UserMapper;
import com.kmacode.identityservice.repository.UserRepository;
import com.kmacode.identityservice.service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserServiceImpl implements UserService {

    UserRepository userRepository;
    UserMapper userMapper;
    PasswordEncoder passwordEncoder;

    @Override
    public UserResponse createUser(UserCreationRequest userCreationRequest) {
        if (userRepository.existsByUsername(userCreationRequest.getUsername())) {
            throw new RuntimeException("Username already exists");
        }
        User user = userMapper.toUser(userCreationRequest);
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        user.setPassword(passwordEncoder.encode(userCreationRequest.getPassword()));

        HashSet<String> roles = new HashSet<>();
        roles.add(Role.USER.name());
//        roles.add(Role.ADMIN.name());
        user.setRoles(roles);

        return userMapper.toUserResponse(userRepository.save(user));
    }

    @Override
    public UserResponse updateUser(String id, UserUpdateRequest userUpdateRequest) {
        User user = userRepository.findById(id).orElseThrow(()-> new AppException(ErrorCode.USER_EXISTED));
        userMapper.updateUser(user, userUpdateRequest);
        return userMapper.toUserResponse(userRepository.save(user));
    }

    @PostAuthorize("returnObject.username == authentication.name || hasRole('ADMIN')"  )
    @Override
    public UserResponse getUser(String id) {
        log.info("In method get user");
        return userMapper.toUserResponse(userRepository.findById(id).orElseThrow(()-> new AppException(ErrorCode.USERNAME_NOT_EXISTED)));
    }

    @Override
    public void deleteUser(String id) {
        userRepository.deleteById(id);
    }


    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public List<UserResponse> getUsers() {
        log.info("In method get users");
        return userRepository.findAll().stream().map(userMapper::toUserResponse).toList();
    }

    @Override
    public UserResponse getMyInfo() {
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();
        User user = userRepository.findByUsername(name).orElseThrow(()-> new AppException(ErrorCode.USERNAME_NOT_EXISTED));
        return userMapper.toUserResponse(user);
    }
}
