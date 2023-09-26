package com.fourttttty.corookie.directmessagechannel.application.service;

import com.fourttttty.corookie.directmessagechannel.application.repository.DirectMessageChannelRepository;
import com.fourttttty.corookie.directmessagechannel.domain.DirectMessageChannel;
import com.fourttttty.corookie.directmessagechannel.dto.response.DirectMessageChannelResponse;
import com.fourttttty.corookie.member.domain.Member;
import com.fourttttty.corookie.project.domain.Project;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class DirectMessageChannelService {

    private final DirectMessageChannelRepository directMessageChannelRepository;

    @Transactional
    public DirectMessageChannelResponse save(Member member1, Member member2, Project project) {
        return DirectMessageChannelResponse.of(directMessageChannelRepository
                .save(DirectMessageChannel.of(true, member1, member2, project)));
    }

    public DirectMessageChannelResponse findById(Long directChannelId) {
        return DirectMessageChannelResponse.of(directMessageChannelRepository.findById(directChannelId)
                .orElseThrow(EntityNotFoundException::new));
    }

    public DirectMessageChannelResponse findByMembersId(Long member1Id, Long member2Id, Long projectId) {
        return DirectMessageChannelResponse.of(directMessageChannelRepository
                .findByMembersId(member1Id, member2Id, projectId)
                .orElseThrow(EntityNotFoundException::new));
    }

    public List<DirectMessageChannelResponse> findByMemberId(Long memberId, Long projectId) {
        return directMessageChannelRepository.findByMemberId(memberId, projectId).stream()
                .map(DirectMessageChannelResponse::of)
                .toList();
    }

    @Transactional
    public void delete(Long directMessageChannelId) {
        directMessageChannelRepository.findById(directMessageChannelId)
                .orElseThrow(EntityNotFoundException::new)
                .delete();
    }
}
