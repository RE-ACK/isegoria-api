package com.isegoria.server.global.resolver;

import org.springframework.core.MethodParameter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.isegoria.server.global.annotations.CurrentUser;
import com.isegoria.server.global.error.ErrorCode;
import com.isegoria.server.global.exception.ApiException;
import com.isegoria.server.global.jwt.JwtPayload;

import org.springframework.lang.Nullable;

@Component
public class CurrentUserResolver implements HandlerMethodArgumentResolver {

  @Override
  public boolean supportsParameter(MethodParameter parameter) {
    boolean hasCurrentUserAnnotation = parameter.hasParameterAnnotation(CurrentUser.class);
    boolean isSupportedType = JwtPayload.class.isAssignableFrom(parameter.getParameterType());
    return hasCurrentUserAnnotation && isSupportedType;
  }

  @Override
  public Object resolveArgument(MethodParameter parameter,
      @Nullable ModelAndViewContainer mavContainer,
      NativeWebRequest webRequest,
      @Nullable WebDataBinderFactory binderFactory) throws Exception {

    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    if (authentication == null || !(authentication.getPrincipal() instanceof JwtPayload)) {
      throw new ApiException(ErrorCode.UNAUTHORIZED);
    }

    Object principal = authentication.getPrincipal();

    if (principal instanceof JwtPayload jwtPayload) {
      return jwtPayload;
    }

    return null;
  }
}