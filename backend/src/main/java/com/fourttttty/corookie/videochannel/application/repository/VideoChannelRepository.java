package com.fourttttty.corookie.videochannel.application.repository;

import com.fourttttty.corookie.videochannel.domain.VideoChannel;

import java.util.List;
import java.util.Optional;

public interface VideoChannelRepository {
    List<VideoChannel> findByProjectId(Long projectId);
    List<VideoChannel> findAll();

    Optional<VideoChannel> findById(Long id);

    VideoChannel save(VideoChannel videoChannel);
}
