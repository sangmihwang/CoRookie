package com.fourttttty.corookie.textchannel.application.repository;

import com.fourttttty.corookie.textchannel.domain.TextChannelPin;
import com.fourttttty.corookie.textchannel.infrastructure.TextChannelPinJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class TextChannelPinRepositoryImpl implements TextChannelPinRepository {

    private final TextChannelPinJpaRepository textChannelPinJpaRepository;

    @Override
    public TextChannelPin save(TextChannelPin textChannelPin) {
        return textChannelPinJpaRepository.save(textChannelPin);
    }

    @Override
    public List<TextChannelPin> findByTextChannelIdAndMemberId(Long textChannelId, Long memberId) {
        return textChannelPinJpaRepository.findByTextChannelIdAndMemberId(textChannelId, memberId);
    }

    @Override
    public boolean exists(Long textChannelId, Long memberId) {
        return textChannelPinJpaRepository.existsByTextChannelIdAndMemberId(textChannelId, memberId);
    }

    @Override
    public void delete(Long textChannelId, Long memberId) {
        textChannelPinJpaRepository.deleteByTextChannelIdAndMemberId(textChannelId, memberId);
    }
}
