package com.fourttttty.corookie.directmessage.application.service;

import com.fourttttty.corookie.directmessage.application.repository.DirectMessageRepository;
import com.fourttttty.corookie.directmessage.domain.DirectMessage;
import com.fourttttty.corookie.directmessage.dto.request.DirectMessageCreateRequest;
import com.fourttttty.corookie.directmessage.dto.response.DirectMessageDetailResponse;
import com.fourttttty.corookie.directmessagechannel.application.repository.DirectMessageChannelRepository;
import com.fourttttty.corookie.member.application.service.MemberService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class DirectMessageService {

    private final DirectMessageRepository directMessageRepository;
    private final DirectMessageChannelRepository directMessageChannelRepository;
    private final MemberService memberService;

    @Transactional
    public DirectMessageDetailResponse create(DirectMessageCreateRequest request) {
        DirectMessage directMessage = DirectMessage.of(request.content(), true,
                directMessageChannelRepository.findById(request.directChannelId())
                        .orElseThrow(EntityNotFoundException::new),
                memberService.findEntityById(request.writerId()));
        return DirectMessageDetailResponse.from(directMessageRepository.save(directMessage));
    }

    public DirectMessageDetailResponse findById(Long directMessageId) {
        return DirectMessageDetailResponse.from(directMessageRepository.findById(directMessageId)
                .orElseThrow(EntityNotFoundException::new));
    }

    public List<DirectMessageDetailResponse> findByDirectChannelIdLatest(Long directChannelId, Pageable pageable) {
        return directMessageRepository.findByDirectChannelIdLatest(directChannelId, pageable).stream()
                .map(DirectMessageDetailResponse::from)
                .toList();
    }

    @Transactional
    public void delete(Long directMessageId) {
        directMessageRepository.findById(directMessageId).orElseThrow(EntityNotFoundException::new).delete();
    }
}
