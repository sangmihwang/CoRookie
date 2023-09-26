package com.fourttttty.corookie.texture.member.application.repository;

import com.fourttttty.corookie.member.application.repository.MemberRepository;
import com.fourttttty.corookie.member.domain.Member;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class FakeMemberRepository implements MemberRepository {
    private long autoIncrementId = 1L;
    private final Map<Long, Member> store = new HashMap<>();

    @Override
    public Optional<Member> findById(Long id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public Optional<Member> findByOAuth2Account(String account) {
        return Optional.empty();
    }

    @Override
    public Member save(Member member) {
        if (member.getId() == null) {
            setIdInEntity(member);
        }
        store.put(autoIncrementId, member);
        autoIncrementId++;
        return member;
    }

    private void setIdInEntity(Member member) {
        try {
            Field id = Member.class.getDeclaredField("id");
            id.setAccessible(true);
            id.set(member, autoIncrementId);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
