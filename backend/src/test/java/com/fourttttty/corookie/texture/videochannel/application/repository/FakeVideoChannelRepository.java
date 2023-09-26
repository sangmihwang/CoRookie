package com.fourttttty.corookie.texture.videochannel.application.repository;

import com.fourttttty.corookie.videochannel.application.repository.VideoChannelRepository;
import com.fourttttty.corookie.videochannel.domain.VideoChannel;

import java.lang.reflect.Field;
import java.util.*;

public class FakeVideoChannelRepository implements VideoChannelRepository {

    private long autoIncrementId = 1L;
    private final Map<Long, VideoChannel> store = new HashMap<>();

    @Override
    public VideoChannel save(VideoChannel videoChannel) {
        setIdInEntity(videoChannel);
        store.put(autoIncrementId, videoChannel);

        return videoChannel;
    }

    @Override
    public List<VideoChannel> findByProjectId(Long projectId) {
        return store.values().stream()
                .filter(videoChannel -> videoChannel.getProject().getId().equals(projectId))
                .toList();
    }

    @Override
    public List<VideoChannel> findAll() {
        return new ArrayList<>(store.values());
    }

    @Override
    public Optional<VideoChannel> findById(Long id) {
        return Optional.ofNullable(store.get(id));
    }

    private void setIdInEntity(VideoChannel videoChannel) {
        try {
            Field id = VideoChannel.class.getDeclaredField("id");
            id.setAccessible(true);
            id.set(videoChannel, 1L);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
