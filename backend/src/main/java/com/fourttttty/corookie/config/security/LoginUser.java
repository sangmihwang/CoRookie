package com.fourttttty.corookie.config.security;

import com.fourttttty.corookie.member.domain.Member;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;

@Getter
public class LoginUser implements OAuth2User {
    private final Member member;
    private final Map<String, Object> attribute;
    private final Collection<? extends GrantedAuthority> authorities;

    public LoginUser(Member member, Map<String, Object> attribute, Collection<? extends GrantedAuthority> authorities) {
        this.member = member;
        this.attribute = attribute;
        this.authorities = authorities;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attribute;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getName() {
        return member.getId().toString();
    }

    public Long getMemberId() {
        return member.getId();
    }
}
