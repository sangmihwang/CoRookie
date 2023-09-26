package com.fourttttty.corookie.config.security.oauth2;

import com.fourttttty.corookie.member.domain.AuthProvider;
import lombok.Getter;

@Getter
public class OAuth2Request {
    private String accountId;
    private String name;
    private String email;
    private String profileImage;
    private AuthProvider authProvider;

    public OAuth2Request(String accountId, String name, String email, String profileImage, AuthProvider authProvider) {
        this.accountId = accountId;
        this.name = name;
        this.email = email;
        this.profileImage = profileImage;
        this.authProvider = authProvider;
    }
}
