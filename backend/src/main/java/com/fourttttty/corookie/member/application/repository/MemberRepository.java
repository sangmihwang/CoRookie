package com.fourttttty.corookie.member.application.repository;


import com.fourttttty.corookie.member.domain.Member;

import java.util.Optional;

public interface MemberRepository {
    Member save(Member member);

    Optional<Member> findById(Long id);

    Optional<Member> findByOAuth2Account(String account);
}
