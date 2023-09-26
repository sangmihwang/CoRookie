package com.fourttttty.corookie.member.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Oauth2 {

    @Enumerated(EnumType.STRING)
    private AuthProvider provider;

    @Column(nullable = false)
    private String account;

    private Oauth2(AuthProvider provider, String account) {
        this.provider = provider;
        this.account = account;
    }

    public static Oauth2 of(AuthProvider provider, String account) {
        return new Oauth2(provider, account);
    }
}
