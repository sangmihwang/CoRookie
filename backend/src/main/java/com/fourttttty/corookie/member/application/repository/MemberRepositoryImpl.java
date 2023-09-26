package com.fourttttty.corookie.member.application.repository;

import com.fourttttty.corookie.member.domain.Member;
import com.fourttttty.corookie.member.infrastructure.MemberJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepository {
    private final MemberJpaRepository memberJpaRepository;

    @Override
    public Member save(Member member) {
        return memberJpaRepository.save(member);
    }

    @Override
    public Optional<Member> findById(Long id) {
        return memberJpaRepository.findById(id);
    }

    @Override
    public Optional<Member> findByOAuth2Account(String account) {
        return memberJpaRepository.findByOauth2Account(account);
    }
}
