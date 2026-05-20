package com.isegoria.server.global.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.Servlet;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class InternalApiKeyAuthenticationFilter extends OncePerRequestFilter {
    private static final String HEADER_NAME = "X-Internal-API-Key";
    private final String intervalApiKey;

    public InternalApiKeyAuthenticationFilter(String intervalApiKey){
        this.intervalApiKey = intervalApiKey;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String apiKey = request.getHeader(HEADER_NAME);

        if(apiKey != null && apiKey.equals(intervalApiKey)){
            JwtPayload systemPayload = new JwtPayload(0L);
            UsernamePasswordAuthenticationToken auth =
                    new UsernamePasswordAuthenticationToken(systemPayload, null, null);
            SecurityContextHolder.getContext().setAuthentication(auth);
        }

        filterChain.doFilter(request, response);
    }
}
