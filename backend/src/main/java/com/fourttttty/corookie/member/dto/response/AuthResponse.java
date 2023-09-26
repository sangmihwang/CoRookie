package com.fourttttty.corookie.member.dto.response;

import com.fourttttty.corookie.config.security.jwt.JwtToken;

public record AuthResponse(String accessToken,
                           String refreshToken) {
    public static AuthResponse from(JwtToken jwtToken) {
        return new AuthResponse(jwtToken.getAccessToken(), jwtToken.getRefreshToken());
    }
}
