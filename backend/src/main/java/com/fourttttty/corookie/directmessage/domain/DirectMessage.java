package com.fourttttty.corookie.directmessage.domain;

import com.fourttttty.corookie.directmessagechannel.domain.DirectMessageChannel;
import com.fourttttty.corookie.global.audit.BaseTime;
import com.fourttttty.corookie.member.domain.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "direct_message")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class DirectMessage extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "direct_message_id")
    private Long id;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private Boolean enabled;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "channel_Id")
    private DirectMessageChannel directMessageChannel;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member writer;

    private DirectMessage(String content, Boolean enabled, DirectMessageChannel directMessageChannel, Member writer) {
        this.content = content;
        this.enabled = enabled;
        this.directMessageChannel = directMessageChannel;
        this.writer = writer;
    }

    public static DirectMessage of(String content, Boolean enabled, DirectMessageChannel directMessageChannel, Member writer) {
        return new DirectMessage(content, enabled, directMessageChannel, writer);
    }

    public void delete() {
        this.enabled = false;
    }
}
