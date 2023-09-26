package com.fourttttty.corookie.videochannel.application.repository;

import com.fourttttty.corookie.videochannel.domain.VideoChannel;
import com.fourttttty.corookie.videochannel.infrastructure.VideoChannelJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class VideoChannelRepositoryImpl implements VideoChannelRepository {

    private final VideoChannelJpaRepository videoChannelJpaRepository;

    @Override
    public List<VideoChannel> findByProjectId(Long projectId) {
        return videoChannelJpaRepository.findByProjectId(projectId);
    }

    @Override
    public List<VideoChannel> findAll() {
        return videoChannelJpaRepository.findAll();
    }

    @Override
    public Optional<VideoChannel> findById(Long id) {
        return videoChannelJpaRepository.findById(id);
    }

    @Override
    public VideoChannel save(VideoChannel videoChannel) {
        return videoChannelJpaRepository.save(videoChannel);
    }
}
