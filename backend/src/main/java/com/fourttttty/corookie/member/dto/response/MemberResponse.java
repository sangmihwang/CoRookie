package com.fourttttty.corookie.member.dto.response;

import com.fourttttty.corookie.member.domain.Member;

public record MemberResponse(Long id,
                             String name,
                             String email,
                             String imageUrl) {

    public static MemberResponse from(Member member) {
        return new MemberResponse(member.getId(), member.getName(), member.getEmail(), member.getImageUrl());
    }
}
