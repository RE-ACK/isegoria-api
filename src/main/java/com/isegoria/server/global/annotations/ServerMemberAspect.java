package com.isegoria.server.global.annotations;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import com.isegoria.server.global.error.ErrorCode;
import com.isegoria.server.global.exception.ApiException;
import com.isegoria.server.global.jwt.JwtPayload;
import com.isegoria.server.server.repository.ServerMemberRepository;
import com.isegoria.server.channel.repository.ChannelRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class ServerMemberAspect {

    private final ServerMemberRepository serverMemberRepository;
    private final ChannelRepository channelRepository;

    @Before("@annotation(com.isegoria.server.global.annotations.ServerMember) || @within(com.isegoria.server.global.annotations.ServerMember)")
    public void validateServerMember(JoinPoint joinPoint) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new ApiException(ErrorCode.UNAUTHORIZED);
        }

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Object[] args = joinPoint.getArgs();

        Long serverId = resolveServerId(signature, args);
        if (serverId == null) {
            log.warn("Failed to resolve serverId from method: {}", signature.getMethod().getName());
            throw new ApiException(ErrorCode.CANNOT_IDENTIFY_SERVER_ID);
        }

        Long userId = extractUserId(authentication);
        if (userId == null || !serverMemberRepository.existsByServerIdAndUserId(serverId, userId)) {
            throw new ApiException(ErrorCode.NOT_MEMBER_IN_THIS_SERVER);
        }
    }

    private Long extractUserId(Authentication authentication) {
        Object principal = authentication.getPrincipal();
        if (principal instanceof JwtPayload jwtPayload) {
            return jwtPayload.getId();
        }
        String subject = authentication.getName();
        try {
            return Long.parseLong(subject);
        } catch (Exception e) {
            return null;
        }
    }

    private Long resolveServerId(MethodSignature signature, Object[] args) {
        String[] parameterNames = signature.getParameterNames();
        if (parameterNames != null) {
            for (int i = 0; i < parameterNames.length; i++) {
                if ("serverId".equals(parameterNames[i]) && args[i] instanceof Long serverId) {
                    return serverId;
                }
                if ("channelId".equals(parameterNames[i]) && args[i] instanceof Long channelId) {
                    return channelRepository.findById(channelId)
                            .map(channel -> channel.getServer().getId())
                            .orElse(null);
                }
            }
        }
        for (Object arg : args) {
            if (arg == null)
                continue;
            Field serverIdField = ReflectionUtils.findField(arg.getClass(), "serverId");
            if (serverIdField != null) {
                try {
                    ReflectionUtils.makeAccessible(serverIdField);
                    Object value = serverIdField.get(arg);
                    if (value instanceof Long serverId) {
                        return serverId;
                    }
                } catch (IllegalAccessException ignored) {
                }
            }
            Field channelIdField = ReflectionUtils.findField(arg.getClass(), "channelId");
            if (channelIdField != null) {
                try {
                    ReflectionUtils.makeAccessible(channelIdField);
                    Object value = channelIdField.get(arg);
                    if (value instanceof Long channelId) {
                        return channelRepository.findById(channelId)
                                .map(channel -> channel.getServer().getId())
                                .orElse(null);
                    }
                } catch (IllegalAccessException ignored) {
                }
            }
        }
        return null;
    }
}
