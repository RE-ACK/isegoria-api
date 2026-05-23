package com.isegoria.server.auth.response;

import com.isegoria.server.user.entity.User;
import com.isegoria.server.user.response.UserResponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthResponse {

  private UserResponse user;
  private TokenResponse serverTokens;

  public static AuthResponse fromEntity(User user, TokenResponse serverTokens) {
    return AuthResponse.builder()
        .user(UserResponse.fromEntity(user))
        .serverTokens(serverTokens)
        .build();
  }
}
