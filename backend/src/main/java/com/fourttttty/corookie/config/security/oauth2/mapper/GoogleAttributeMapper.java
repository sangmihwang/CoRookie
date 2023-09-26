package com.fourttttty.corookie.config.security.oauth2.mapper;

import com.fourttttty.corookie.config.security.oauth2.OAuth2Request;
import com.fourttttty.corookie.member.domain.AuthProvider;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class GoogleAttributeMapper implements AttributeMapper {
    @Override
    public OAuth2Request mapToDto(Map<String, Object> attributes) {
        String accountId = (String) attributes.get("sub");
        String name = (String) attributes.get("name");
        String email = (String) attributes.get("email");
        String imageUrl = (String) attributes.get("picture");
        return new OAuth2Request(accountId, name, email, imageUrl, AuthProvider.GOOGLE);
    }
}
