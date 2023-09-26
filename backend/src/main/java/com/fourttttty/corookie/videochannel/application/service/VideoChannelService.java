package com.fourttttty.corookie.videochannel.application.service;

import com.fourttttty.corookie.project.application.repository.ProjectRepository;
import com.fourttttty.corookie.videochannel.application.repository.VideoChannelRepository;
import com.fourttttty.corookie.videochannel.domain.VideoChannel;
import com.fourttttty.corookie.videochannel.dto.request.VideoChannelCreateRequest;
import com.fourttttty.corookie.videochannel.dto.request.VideoChannelModifyRequest;
import com.fourttttty.corookie.videochannel.dto.response.VideoChannelResponse;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class VideoChannelService {

    private final VideoChannelRepository videoChannelRepository;
    private final ProjectRepository projectRepository;

    public List<VideoChannelResponse> findByProjectId(Long projectId) {
        return videoChannelRepository.findByProjectId(projectId).stream()
                .map(VideoChannelResponse::from)
                .toList();
    }

    public VideoChannelResponse findById(Long id) {
        return VideoChannelResponse.from(videoChannelRepository
                .findById(id)
                .orElseThrow(EntityNotFoundException::new)
        );
    }

    @Transactional
    public VideoChannelResponse create(VideoChannelCreateRequest request, Long projectId) {
        return VideoChannelResponse.from(videoChannelRepository.save(request
                .toEntity(projectRepository
                        .findById(projectId)
                        .orElseThrow(EntityNotFoundException::new))));
    }

    @Transactional
    public VideoChannelResponse modify(Long videoChannelId, VideoChannelModifyRequest request) {
        VideoChannel videoChannel = videoChannelRepository.findById(videoChannelId)
                .orElseThrow(EntityNotFoundException::new);
        videoChannel.modifyChannelName(request.name());
        return VideoChannelResponse.from(videoChannel);
    }

    @Transactional
    public void delete(Long videoChannelId) {
        VideoChannel videoChannel = videoChannelRepository.findById(videoChannelId)
                .orElseThrow(EntityNotFoundException::new);
        videoChannel.deleteChannel();
    }
}
