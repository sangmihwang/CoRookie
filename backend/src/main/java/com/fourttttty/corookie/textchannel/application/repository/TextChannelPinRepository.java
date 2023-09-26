package com.fourttttty.corookie.textchannel.application.repository;

import com.fourttttty.corookie.textchannel.domain.TextChannelPin;

import java.util.List;
import java.util.Optional;

public interface TextChannelPinRepository {

    TextChannelPin save(TextChannelPin textChannelPin);
    List<TextChannelPin> findByTextChannelIdAndMemberId(Long textChannelId, Long memberId);
    boolean exists(Long textChannelId, Long memberId);
    void delete(Long textChannelId, Long memberId);
}
