package com.fourttttty.corookie.member.application.service;

import com.fourttttty.corookie.config.security.oauth2.OAuth2Request;
import com.fourttttty.corookie.member.application.repository.MemberRepository;
import com.fourttttty.corookie.member.domain.Member;
import com.fourttttty.corookie.member.domain.Oauth2;
import com.fourttttty.corookie.member.dto.request.MemberNameUpdateRequest;
import com.fourttttty.corookie.member.dto.request.MemberProfileUpdateRequest;
import com.fourttttty.corookie.member.dto.response.MemberResponse;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {
    private final MemberRepository memberRepository;

    public Member save(OAuth2Request oAuth2Request) {
        return memberRepository.save(Member.of(oAuth2Request.getName(), oAuth2Request.getEmail(), oAuth2Request.getProfileImage(),
                Oauth2.of(oAuth2Request.getAuthProvider(), oAuth2Request.getAccountId())));
    }

    public Member findEntityById(Long id) {
        return memberRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    @Transactional
    public Member saveIfNotExists(OAuth2Request oAuth2Request) {
        return memberRepository.findByOAuth2Account(oAuth2Request.getAccountId())
                .orElseGet(() -> save(oAuth2Request));
    }

    @Transactional
    public MemberResponse updateName(MemberNameUpdateRequest request, Long memberId) {
        Member member = findEntityById(memberId);
        member.updateName(request.name());

        return MemberResponse.from(member);
    }

    @Transactional
    public MemberResponse updateProfileUrl(MemberProfileUpdateRequest request, Long memberId) {
        Member member = findEntityById(memberId);
        member.updateImageUrl(request.imageUrl());

        return MemberResponse.from(member);
    }
}
