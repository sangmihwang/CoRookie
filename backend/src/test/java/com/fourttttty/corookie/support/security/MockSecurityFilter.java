package com.fourttttty.corookie.support.security;

import com.fourttttty.corookie.config.security.LoginUser;
import com.fourttttty.corookie.member.domain.AuthProvider;
import com.fourttttty.corookie.member.domain.Member;
import com.fourttttty.corookie.member.domain.Oauth2;
import jakarta.servlet.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;
import java.lang.reflect.Field;

public class MockSecurityFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) {}

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        SecurityContext context = SecurityContextHolder.getContext();

        LoginUser loginUser = new LoginUser(createMember(), null, null);
        context.setAuthentication(new UsernamePasswordAuthenticationToken(loginUser, "password", loginUser.getAuthorities()));

        chain.doFilter(request, response);
    }

    private Member createMember() {
        Member member = Member.of("memberName", "test@gmail.com", "https://test", Oauth2.of(AuthProvider.KAKAO, "account"));
        Class<Member> memberClass = Member.class;
        try {
            Field id = memberClass.getDeclaredField("id");
            id.setAccessible(true);
            id.set(member, 1L);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        return member;
    }

    @Override
    public void destroy() {
        SecurityContextHolder.clearContext();
    }
}
