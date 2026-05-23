package com.isegoria.server.user.response;

import java.time.LocalDateTime;

import com.isegoria.server.user.entity.User;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class UserResponse {

  private Long id;

  private String username;

  private String email;

  private String avatarUrl;

  private LocalDateTime createdAt;

  public static UserResponse fromEntity(User user) {
    return UserResponse.builder()
        .id(user.getId())
        .username(user.getUsername())
        .email(user.getEmail())
        .avatarUrl(user.getAvatarUrl())
        .createdAt(user.getCreatedAt())
        .build();
  }
}