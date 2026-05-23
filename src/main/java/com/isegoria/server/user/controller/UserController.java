package com.isegoria.server.user.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.isegoria.server.global.annotations.CurrentUser;
import com.isegoria.server.global.api.Api;
import com.isegoria.server.global.jwt.JwtPayload;
import com.isegoria.server.global.message.ResponseMessage;
import com.isegoria.server.user.request.UpdateUserRequest;
import com.isegoria.server.user.response.UserResponse;
import com.isegoria.server.user.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("user")
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;

  /**
   * 세션 정보 조회
   * 
   * @param JwtPayload user
   * @return UserResponse
   *         - Long id,
   *         - String username,
   *         - String email,
   *         - String avatarUrl,
   *         - LocalDateTime createdAt
   */
  @GetMapping("me")
  public Api<UserResponse> getSession(@CurrentUser JwtPayload user) {
    UserResponse response = userService.getSession(user.getId());
    return Api.OK(response);
  }

  /**
   * 
   * @param UpdateUserRequest request
   *                          - String username,
   *                          - String avatarUrl
   * @param JwtPayload        user
   * @return UserResponse
   *         - Long id,
   *         - String username,
   *         - String email,
   *         - String avatarUrl,
   *         - LocalDateTime createdAt
   */
  @PutMapping("update")
  public Api<UserResponse> updateUser(@Valid @RequestBody UpdateUserRequest request, @CurrentUser JwtPayload user) {
    UserResponse response = userService.updateUser(user.getId(), request);
    return Api.OK(response, ResponseMessage.UPDATE_USER_SUCCESS);
  }

  /**
   * 회원 탈퇴
   * 
   * @param JwtPayload user
   */
  @DeleteMapping("delete")
  public Api<Void> deleteUser(@CurrentUser JwtPayload user) {
    userService.deleteUser(user.getId());
    return Api.OK(ResponseMessage.DELETE_USER_SUCCESS);
  }
}
