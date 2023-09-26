package com.fourttttty.corookie.textchannel.infrastructure;

import com.fourttttty.corookie.textchannel.domain.TextChannelPin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TextChannelPinJpaRepository extends JpaRepository<TextChannelPin, Long> {
    List<TextChannelPin> findByTextChannelIdAndMemberId(Long textChannelId, Long memberId);

    boolean existsByTextChannelIdAndMemberId(Long textChannelId, Long memberId);
    void deleteByTextChannelIdAndMemberId(Long textChannelId, Long memberId);
}
