package com.isegoria.server.user.request;

import com.isegoria.server.user.entity.User;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateUserRequest {

  @NotBlank(message = "이름은 필수 입력 항목입니다.")
  private String username;

  private String avatarUrl;

  public static User toEntity(UserRequest request, String encodedPassword) {
    return User.builder()
        .username(request.getUsername())
        .email(request.getEmail())
        .password(encodedPassword)
        .avatarUrl(request.getAvatarUrl())
        .build();
  }
}