package com.fourttttty.corookie.config.security.jwt;

import com.fourttttty.corookie.config.security.LoginUser;
import com.fourttttty.corookie.config.security.oauth2.mapper.LoginUserMapper;
import com.fourttttty.corookie.member.application.service.MemberService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.security.Key;

@Component
@RequiredArgsConstructor
public class JwtValidator {

    private final Key key;
    private final MemberService memberService;
    private final LoginUserMapper loginUserMapper;

    public Authentication getAuthentication(String accessToken) {
        LoginUser loginUser = getLoginUser(accessToken);
        return new UsernamePasswordAuthenticationToken(loginUser, "", loginUser.getAuthorities());
    }

    public LoginUser getLoginUser(String token) {
        return loginUserMapper.toLoginUser(
                memberService.findEntityById(Long.parseLong(
                        getTokenClaims(token).get("id", String.class))));
    }

    private Claims getTokenClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
