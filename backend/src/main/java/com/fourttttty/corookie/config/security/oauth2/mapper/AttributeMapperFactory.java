package com.fourttttty.corookie.config.security.oauth2.mapper;

import com.fourttttty.corookie.member.domain.AuthProvider;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.Map;

@Component
public class AttributeMapperFactory {

    private final Map<AuthProvider, AttributeMapper> mapperMap = new EnumMap<>(AuthProvider.class);
    private final KakaoAttributeMapper kakaoAttributeMapper;
    private final GoogleAttributeMapper googleAttributeMapper;
    private final GithubAttributeMapper githubAttributeMapper;

    public AttributeMapperFactory(KakaoAttributeMapper kakaoAttributeMapper,
                                  GoogleAttributeMapper googleAttributeMapper,
                                  GithubAttributeMapper githubAttributeMapper) {
        this.kakaoAttributeMapper = kakaoAttributeMapper;
        this.googleAttributeMapper = googleAttributeMapper;
        this.githubAttributeMapper = githubAttributeMapper;
        initialize();
    }

    private void initialize() {
        mapperMap.put(AuthProvider.KAKAO, kakaoAttributeMapper);
        mapperMap.put(AuthProvider.GOOGLE, googleAttributeMapper);
        mapperMap.put(AuthProvider.GITHUB, githubAttributeMapper);
    }

    public AttributeMapper getAttributeMapper(AuthProvider authProvider) {
        return mapperMap.get(authProvider);
    }
}
