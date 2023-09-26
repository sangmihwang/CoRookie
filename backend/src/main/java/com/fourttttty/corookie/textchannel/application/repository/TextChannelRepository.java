package com.fourttttty.corookie.textchannel.application.repository;

import com.fourttttty.corookie.textchannel.domain.TextChannel;
import com.fourttttty.corookie.textchannel.domain.TextChannelPin;

import java.util.List;
import java.util.Optional;

public interface TextChannelRepository {
    List<TextChannel> findAll();

    List<TextChannel> findByProjectId(Long projectId, Long memberId);

    Optional<TextChannel> findById(Long id);

    TextChannel save(TextChannel textChannel);
}
