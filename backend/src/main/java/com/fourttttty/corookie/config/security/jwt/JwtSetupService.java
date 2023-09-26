package com.fourttttty.corookie.config.security.jwt;

import com.fourttttty.corookie.config.security.LoginUser;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class JwtSetupService {
    private final JwtProvider jwtProvider;

    @Value("${client.host}")
    private String clientHost;

    @Value("${jwt.access-header}")
    private String accessTokenHeaderTag;

    @Value("${jwt.refresh-header}")
    private String refreshTokenHeaderTag;

    public void addJwtTokensToCookie(HttpServletResponse response, LoginUser loginUser) {
        JwtToken jwtToken = jwtProvider.createJwtToken(loginUser);

        ResponseCookie accessTokenCookie = setCookie(accessTokenHeaderTag, jwtToken.getAccessToken());
        ResponseCookie refreshTokenCookie = setCookie(refreshTokenHeaderTag, jwtToken.getRefreshToken());
        log.info("accessToken = {}", accessTokenCookie);
        log.info("refreshToken = {}", refreshTokenCookie);
        response.addHeader("Set-Cookie", accessTokenCookie.toString());
        response.addHeader("Set-Cookie", refreshTokenCookie.toString());
    }

    public JwtToken createJwtTokenByAuthToken(String authToken) {
        return jwtProvider.createJwtTokenByAuthToken(authToken);
    }

    public String createAuthToken(LoginUser loginUser) {
        return jwtProvider.createAuthToken(loginUser);
    }

    private ResponseCookie setCookie(String key, String value) {
        return ResponseCookie.from(key, value)
                .path("/")
                .domain(clientHost)
                .httpOnly(true)
                .sameSite("None")
                .secure(true)
                .build();
    }
}
