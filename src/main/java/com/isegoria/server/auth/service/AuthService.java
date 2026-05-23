package com.isegoria.server.auth.service;

import com.isegoria.server.auth.request.AuthRequest;
import com.isegoria.server.auth.response.AuthResponse;
import com.isegoria.server.auth.response.TokenResponse;
import com.isegoria.server.user.request.UserRequest;
import com.isegoria.server.user.response.UserResponse;

public interface AuthService {

  UserResponse register(UserRequest request);

  AuthResponse login(AuthRequest request);

  TokenResponse refresh(String authorization);

}
