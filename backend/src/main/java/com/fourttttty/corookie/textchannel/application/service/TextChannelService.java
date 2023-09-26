package com.fourttttty.corookie.textchannel.application.service;

import com.fourttttty.corookie.global.exception.InvalidChannelPinRequestException;
import com.fourttttty.corookie.member.application.repository.MemberRepository;
import com.fourttttty.corookie.member.domain.Member;
import com.fourttttty.corookie.project.application.repository.ProjectMemberRepository;
import com.fourttttty.corookie.project.application.repository.ProjectRepository;
import com.fourttttty.corookie.textchannel.application.repository.TextChannelPinRepository;
import com.fourttttty.corookie.textchannel.application.repository.TextChannelRepository;
import com.fourttttty.corookie.textchannel.domain.TextChannel;
import com.fourttttty.corookie.textchannel.domain.TextChannelPin;
import com.fourttttty.corookie.textchannel.dto.request.TextChannelCreateRequest;
import com.fourttttty.corookie.textchannel.dto.request.TextChannelModifyRequest;
import com.fourttttty.corookie.textchannel.dto.response.TextChannelResponse;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TextChannelService {

    private final TextChannelRepository textChannelRepository;
    private final ProjectRepository projectRepository;
    private final TextChannelPinRepository textChannelPinRepository;
    private final ProjectMemberRepository projectMemberRepository;
    private final MemberRepository memberRepository;

    public TextChannelResponse findById(Long id, Long memberId) {
        return TextChannelResponse.from(textChannelRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new), textChannelPinRepository.exists(id, memberId));
    }

    public List<TextChannelResponse> findByProjectId(Long projectId, Long memberId) {
        return textChannelRepository.findByProjectId(projectId, memberId).stream()
                .map(textChannel -> TextChannelResponse.from(textChannel,
                        textChannelPinRepository.exists(textChannel.getId(), memberId)))
                .toList();
    }

    @Transactional
    public TextChannelResponse create(TextChannelCreateRequest request, Long projectId) {
        return TextChannelResponse.from(textChannelRepository.save(
                request.toEntity(projectRepository.findById(projectId)
                        .orElseThrow(EntityNotFoundException::new))), false);
    }

    @Transactional
    public void createDefaultChannel(String channelName, Long projectId) {
        TextChannel.of(channelName, true, false,
                projectRepository.findById(projectId).orElseThrow(EntityNotFoundException::new))
                .changeNotDeletableChannel();
    }

    @Transactional
    public TextChannelResponse modify(Long id, TextChannelModifyRequest request, Long memberId) {
        TextChannel textChannel = textChannelRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        textChannel.modifyChannelName(request.name());
        return TextChannelResponse.from(textChannel, textChannelPinRepository.exists(textChannel.getId(), memberId));
    }

    @Transactional
    public void delete(Long id) {
        TextChannel textChannel = textChannelRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        textChannel.deleteChannel();
    }

    @Transactional
    public void pinChannel(Long projectId, Long textChannelId, Long memberId) {
        validateProjectMember(projectId, memberId);
        TextChannel textChannel = textChannelRepository.findById(textChannelId).orElseThrow(EntityNotFoundException::new);
        Member member = memberRepository.findById(memberId).orElseThrow(EntityNotFoundException::new);
        textChannelPinRepository.save(TextChannelPin.of(textChannel, member));
    }

    @Transactional
    public void unpinChannel(Long projectId, Long textChannelId, Long memberId) {
        validateProjectMember(projectId, memberId);
        textChannelPinRepository.delete(textChannelId, memberId);
    }

    private void validateProjectMember(Long projectId, Long memberId) {
        if (!projectMemberRepository.existsMemberInProject(projectId, memberId)) {
            throw new InvalidChannelPinRequestException();
        }
    }
}
