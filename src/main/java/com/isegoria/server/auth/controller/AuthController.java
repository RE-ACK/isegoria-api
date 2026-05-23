package com.isegoria.server.auth.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.isegoria.server.auth.request.AuthRequest;
import com.isegoria.server.auth.response.AuthResponse;
import com.isegoria.server.auth.response.TokenResponse;
import com.isegoria.server.auth.service.AuthService;
import com.isegoria.server.global.api.Api;
import com.isegoria.server.global.message.ResponseMessage;
import com.isegoria.server.user.request.UserRequest;
import com.isegoria.server.user.response.UserResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
public class AuthController {

  private final AuthService authService;

  /**
   * 회원가입
   * 
   * @param UserRequest
   *                    - String email,
   *                    - String username,
   *                    - String password
   * @return UserResponse
   *         - Long id
   *         - String username,
   *         - String email,
   *         - String avatarUrl,
   *         - LocalDateTime createdAt
   */
  @PostMapping("register")
  public Api<UserResponse> register(@Valid @RequestBody UserRequest request) {
    UserResponse response = authService.register(request);
    return Api.OK(response, ResponseMessage.REGISTER_SUCCESS);
  }

  /**
   * 로그인
   * 
   * @param AuthRequest
   *                    - String email,
   *                    - String password
   * @return AuthResponse
   *         - Long id
   *         - String username
   *         - String email
   *         - String avatarUrl
   *         - LocalDateTime createdAt
   *         - TokenResponse serverTokens
   *         - String accessToken
   *         - String refreshToken
   */
  @PostMapping("login")
  public Api<AuthResponse> login(@Valid @RequestBody AuthRequest request) {
    AuthResponse response = authService.login(request);
    return Api.OK(response, ResponseMessage.LOGIN_SUCCESS);
  }

  /**
   * 토큰 재발급
   * 
   * @apiNote 클라이언트는 Authorization 헤더에 "Refresh {refreshToken}" 형식으로 리프레시 토큰을 전달해야
   *          합니다.
   * @param String authorization
   * @return TokenResponse
   *         - String accessToken
   *         - String refreshToken
   */
  @PostMapping("refresh")
  public Api<TokenResponse> refresh(@RequestHeader("Authorization") String authorization) {
    TokenResponse response = authService.refresh(authorization);
    return Api.OK(response);
  }
}
