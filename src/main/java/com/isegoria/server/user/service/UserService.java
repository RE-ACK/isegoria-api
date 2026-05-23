package com.isegoria.server.user.service;

import com.isegoria.server.user.entity.User;
import com.isegoria.server.user.request.UpdateUserRequest;
import com.isegoria.server.user.request.UserRequest;
import com.isegoria.server.user.response.UserResponse;

public interface UserService {

  UserResponse register(UserRequest request);

  User findByEmail(String email);

  User findById(Long id);

  UserResponse getSession(Long id);

  UserResponse updateUser(Long id, UpdateUserRequest request);

  void deleteUser(Long id);
}
