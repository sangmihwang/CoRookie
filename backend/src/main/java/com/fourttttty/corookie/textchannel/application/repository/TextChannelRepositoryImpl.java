package com.fourttttty.corookie.textchannel.application.repository;

import com.fourttttty.corookie.textchannel.domain.TextChannel;
import com.fourttttty.corookie.textchannel.infrastructure.TextChannelJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class TextChannelRepositoryImpl implements TextChannelRepository {

    private final TextChannelJpaRepository textChannelJpaRepository;

    @Override
    public List<TextChannel> findAll() {
        return textChannelJpaRepository.findAll();
    }

    @Override
    public List<TextChannel> findByProjectId(Long projectId, Long memberId) {
        return textChannelJpaRepository.findByProjectIdOrderByPinAndName(projectId, memberId);
    }

    @Override
    public Optional<TextChannel> findById(Long id) {
        return textChannelJpaRepository.findById(id);
    }

    @Override
    public TextChannel save(TextChannel textChannel) {
        return textChannelJpaRepository.save(textChannel);
    }

}
