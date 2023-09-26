package com.fourttttty.corookie.directmessagechannel.application.repository;

import com.fourttttty.corookie.directmessagechannel.domain.DirectMessageChannel;
import com.fourttttty.corookie.directmessagechannel.infrastructure.DirectMessageChannelJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class DirectMessageChannelRepositoryImpl implements DirectMessageChannelRepository {

    private final DirectMessageChannelJpaRepository directMessageChannelJpaRepository;

    @Override
    public DirectMessageChannel save(DirectMessageChannel directMessageChannel) {
        return directMessageChannelJpaRepository.save(directMessageChannel);
    }

    @Override
    public Optional<DirectMessageChannel> findById(Long directMessageChannelId) {
        return directMessageChannelJpaRepository.findById(directMessageChannelId);
    }

    @Override
    public Optional<DirectMessageChannel> findByMembersId(Long member1Id, Long member2Id, Long projectId) {
        return directMessageChannelJpaRepository.findByProjectIdAndMember1IdAndMember2Id(member1Id, member2Id, projectId);
    }

    @Override
    public List<DirectMessageChannel> findByMemberId(Long memberId, Long projectId) {
        return directMessageChannelJpaRepository.findByProjectIdAndMemberId(memberId, projectId);
    }
}
