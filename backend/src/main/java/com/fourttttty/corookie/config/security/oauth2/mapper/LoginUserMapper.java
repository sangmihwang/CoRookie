package com.fourttttty.corookie.config.security.oauth2.mapper;

import com.fourttttty.corookie.config.security.LoginUser;
import com.fourttttty.corookie.member.domain.Member;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class LoginUserMapper {

    public LoginUser toLoginUser(Member member) {
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("id", member.getId());
        return new LoginUser(member, attributes, member.getRole());
    }
}
