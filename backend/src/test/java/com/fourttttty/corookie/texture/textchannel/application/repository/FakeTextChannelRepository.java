package com.fourttttty.corookie.texture.textchannel.application.repository;

import com.fourttttty.corookie.project.application.repository.ProjectRepository;
import com.fourttttty.corookie.project.domain.Project;
import com.fourttttty.corookie.textchannel.application.repository.TextChannelRepository;
import com.fourttttty.corookie.textchannel.domain.TextChannel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class FakeTextChannelRepository implements TextChannelRepository {

    private long autoIncrementId = 1L;
    private final Map<Long, TextChannel> store = new HashMap<>();

    @Override
    public List<TextChannel> findAll() {
        return store.values().stream()
                .toList();
    }

    @Override
    public List<TextChannel> findByProjectId(Long projectId, Long memberId) {
        return store.values().stream()
                .filter(textChannel -> textChannel.getProject().getId().equals(projectId))
                .toList();
    }

    @Override
    public Optional<TextChannel> findById(Long id) { return Optional.ofNullable(store.get(id)); }

    @Override
    public TextChannel save(TextChannel textChannel) {
        store.put(autoIncrementId++, textChannel);
        return textChannel;
    }
}
