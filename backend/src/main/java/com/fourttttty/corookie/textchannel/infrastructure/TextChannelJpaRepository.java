package com.fourttttty.corookie.textchannel.infrastructure;

import com.fourttttty.corookie.textchannel.domain.TextChannel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TextChannelJpaRepository extends JpaRepository<TextChannel, Long> {

    @Query("select t " +
            "from TextChannel t left join TextChannelPin tp on t.id = tp.textChannel.id and tp.member.id = :memberId " +
            "where t.project.id = :projectId " +
            "order by tp.textChannel.id desc, t.channelName")
    List<TextChannel> findByProjectIdOrderByPinAndName(Long projectId, Long memberId);
}
