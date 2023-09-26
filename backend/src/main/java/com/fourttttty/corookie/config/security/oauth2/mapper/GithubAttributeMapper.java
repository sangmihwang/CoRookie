package com.fourttttty.corookie.config.security.oauth2.mapper;

import com.fourttttty.corookie.config.security.oauth2.OAuth2Request;
import com.fourttttty.corookie.member.domain.AuthProvider;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class GithubAttributeMapper implements AttributeMapper {
    @Override
    public OAuth2Request mapToDto(Map<String, Object> attributes) {
        for (Map.Entry<String, Object> stringObjectEntry : attributes.entrySet()) {
            System.out.println("stringObjectEntry.getKey() = " + stringObjectEntry.getKey());
            System.out.println("stringObjectEntry.getValue() = " + stringObjectEntry.getValue());
        }
        String accountId = String.valueOf(attributes.get("id"));
        String name = (String) attributes.get("name");
        String email = (String) attributes.get("email");
        String imageUrl = (String) attributes.get("avatar_url");
        return new OAuth2Request(accountId, name, email, imageUrl, AuthProvider.GOOGLE);
    }
}
