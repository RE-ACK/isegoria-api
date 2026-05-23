package com.isegoria.server.user.service;

import java.util.List;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.isegoria.server.global.error.ErrorCode;
import com.isegoria.server.global.exception.ApiException;
import com.isegoria.server.image.service.ImageService;
import com.isegoria.server.user.entity.User;
import com.isegoria.server.user.repository.UserRepository;
import com.isegoria.server.user.request.UpdateUserRequest;
import com.isegoria.server.user.request.UserRequest;
import com.isegoria.server.user.response.UserResponse;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
  Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final ImageService imageService;

  /**
   * 회원가입
   * 
   * @param UserRequest
   *                    - String email,
   *                    - String username,
   *                    - String password
   * @return UserResponse
   *         - Long id,
   *         - String username,
   *         - String email,
   *         - String avatarUrl,
   *         - LocalDateTime createdAt
   */
  @Override
  public UserResponse register(UserRequest request) {
    String email = request.getEmail();
    User user = this.findByEmail(email);
    if (user != null) {
      throw new ApiException(ErrorCode.EMAIL_ALREADY_EXISTS);
    }
    String encodedPassword = passwordEncoder.encode(request.getPassword());
    User newUser = UserRequest.toEntity(request, encodedPassword);
    userRepository.save(newUser);
    UserResponse response = UserResponse.fromEntity(newUser);
    return response;
  }

  /**
   * 이메일로 사용자 조회
   * 
   * @param String email
   * @return User Entity
   *         - Long id,
   *         - String username,
   *         - String email,
   *         - String password,
   *         - String avatarUrl,
   *         - LocalDateTime createdAt
   */
  @Override
  public User findByEmail(String email) {
    User user = userRepository.findByEmail(email);
    return user;
  }

  @Override
  public User findById(Long id) {
    User user = userRepository.findById(id).orElseThrow(() -> new ApiException(ErrorCode.USER_NOT_FOUND));
    return user;
  }

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
  @Override
  public UserResponse getSession(Long id) {
    User user = this.findById(id);
    UserResponse response = UserResponse.fromEntity(user);
    return response;
  }

  @Override
  public UserResponse updateUser(Long id, UpdateUserRequest request) {
    User user = this.findById(id);
    user.setUsername(request.getUsername());

    String newAvatarUrl = request.getAvatarUrl();
    String currentAvatarUrl = user.getAvatarUrl();

    if (newAvatarUrl != null && !newAvatarUrl.trim().isEmpty()) {

      if (!Objects.equals(newAvatarUrl, currentAvatarUrl)) {

        log.info("아바타 URL 변경 감지: {} → {}", currentAvatarUrl, newAvatarUrl);

        if (currentAvatarUrl != null) {
          List<String> images = imageService.updateImage(
              id.toString(),
              List.of(newAvatarUrl),
              List.of(currentAvatarUrl),
              "user");
          user.setAvatarUrl(images.get(0));
        } else {
          List<String> images = imageService.createImage(
              id.toString(),
              List.of(newAvatarUrl),
              "user");
          user.setAvatarUrl(images.get(0));
        }
      } else {
        log.debug("아바타 URL이 동일하여 스킵합니다.");
      }
    }

    userRepository.save(user);

    UserResponse response = UserResponse.fromEntity(user);
    return response;
  }

  @Transactional
  @Override
  public void deleteUser(Long id) {
    User user = this.findById(id);
    userRepository.delete(user);

    boolean exists = userRepository.existsById(id);
    if (exists) {
      throw new ApiException(ErrorCode.DELETE_USER_FAILED);
    } else {
      imageService.deleteImage(id.toString(), "user");
    }
  }
}
