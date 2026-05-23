package com.isegoria.server.auth.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.isegoria.server.user.service.UserService;

import io.jsonwebtoken.Claims;

import com.isegoria.server.auth.request.AuthRequest;
import com.isegoria.server.auth.response.AuthResponse;
import com.isegoria.server.auth.response.TokenResponse;
import com.isegoria.server.global.error.ErrorCode;
import com.isegoria.server.global.exception.ApiException;
import com.isegoria.server.global.jwt.JwtProvider;
import com.isegoria.server.user.entity.User;
import com.isegoria.server.user.request.UserRequest;
import com.isegoria.server.user.response.UserResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

  private final UserService userService;
  private final PasswordEncoder passwordEncoder;
  private final JwtProvider jwtProvider;

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
  @Override
  public UserResponse register(UserRequest request) {
    UserResponse response = userService.register(request);
    return response;
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
  @Override
  public AuthResponse login(AuthRequest request) {
    String email = request.getEmail();
    String password = request.getPassword();
    User user = userService.findByEmail(email);

    if (user == null) {
      throw new ApiException(ErrorCode.LOGIN_FAILED);
    }
    if (!passwordEncoder.matches(password, user.getPassword())) {
      throw new ApiException(ErrorCode.LOGIN_FAILED);
    }

    TokenResponse tokens = generateTokens(user.getId());
    AuthResponse response = AuthResponse.fromEntity(user, tokens);
    return response;
  }

  /**
   * 토큰 생성
   * 
   * @param Long userId
   * @return TokenResponse
   *         - String accessToken
   *         - String refreshToken
   */
  private TokenResponse generateTokens(Long userId) {
    String accessToken = jwtProvider.createAccessToken(userId);
    String refreshToken = jwtProvider.createRefreshToken(userId);

    return TokenResponse.from(accessToken, refreshToken);
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
  @Override
  public TokenResponse refresh(String authorization) {
    String oldRefreshToken = parserRefreshToken(authorization);
    if (oldRefreshToken == null) {
      throw new ApiException(ErrorCode.INVALID_REFRESH_TOKEN);
    }
    Claims claims = jwtProvider.validateRefreshToken(oldRefreshToken);
    if (claims == null) {
      throw new ApiException(ErrorCode.INVALID_REFRESH_TOKEN);
    }
    Long userId = Long.parseLong(claims.getSubject());
    User user = userService.findById(userId);
    TokenResponse newTokens = generateTokens(user.getId());
    return newTokens;
  }

  private String parserRefreshToken(String authorization) {
    final String REFRESH_PREFIX = "Refresh ";
    final int PREFIX_LENGTH = REFRESH_PREFIX.length();
    if (StringUtils.hasText(authorization) && authorization.startsWith(REFRESH_PREFIX)) {
      if (authorization.length() > PREFIX_LENGTH) {
        return authorization.substring(PREFIX_LENGTH);
      }
    }
    return null;
  }
}
