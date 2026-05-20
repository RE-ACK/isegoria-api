package com.isegoria.server.global.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.isegoria.server.global.api.Api;
import com.isegoria.server.global.error.ErrorCode;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

  private final ObjectMapper objectMapper;

  @Override
  public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
      throws IOException {

    log.warn("인증 실패 - PATH: {}", request.getRequestURI());

    response.setStatus(ErrorCode.UNAUTHORIZED.getHttpStatusCode());
    response.setContentType("application/json;charset=UTF-8");
    response.setCharacterEncoding("UTF-8");

    Api<Object> errorResponse = Api.ERROR(ErrorCode.UNAUTHORIZED);

    objectMapper.writeValue(response.getWriter(), errorResponse);
  }
}