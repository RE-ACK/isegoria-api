package com.isegoria.server.user.request;

import com.isegoria.server.user.entity.User;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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
public class UserRequest {

  @NotBlank(message = "이메일은 필수 입력 항목입니다.")
  @Email(message = "유효한 이메일 형식이 아닙니다.")
  private String email;

  @NotBlank(message = "이름은 필수 입력 항목입니다.")
  private String username;

  @Size(min = 8, max = 20, message = "비밀번호는 8자 이상 20자 이하이어야 합니다.")
  @NotBlank(message = "비밀번호는 필수 입력 항목입니다.")
  private String password;

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