package com.fourttttty.corookie.config.security.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final String AUTHORIZATION_TAG = "Authorization";
    private final JwtValidator jwtValidator;

    @Value("${jwt.access-header}")
    private String accessTokenHeaderTag;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        Optional<String> token = Optional.ofNullable(getTokensFromHeader(request));

        token.ifPresent(
                t -> {
                    log.info("[JwtAuthenticationFilter] AccessToken: {}", t);
                    SecurityContextHolder.getContext().setAuthentication(jwtValidator.getAuthentication(t));
                }
        );

        filterChain.doFilter(request, response);
    }

    private String getTokensFromHeader(HttpServletRequest request) {
        return request.getHeader(AUTHORIZATION_TAG);
    }
}
