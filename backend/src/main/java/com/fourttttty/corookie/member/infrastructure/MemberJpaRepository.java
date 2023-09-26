package com.fourttttty.corookie.member.infrastructure;

import com.fourttttty.corookie.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberJpaRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByOauth2Account(String account);
}
