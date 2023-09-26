package com.fourttttty.corookie.thread.domain;

import com.fourttttty.corookie.global.audit.BaseTime;
import com.fourttttty.corookie.member.domain.Member;
import com.fourttttty.corookie.textchannel.domain.TextChannel;
import com.fourttttty.corookie.thread.dto.request.ThreadModifyRequest;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Thread extends BaseTime {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "thread_id")
    private Long id;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private Boolean enabled;

    @Column(nullable = false)
    private Integer commentCount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "channel_Id")
    private TextChannel textChannel;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member writer;

    private Thread(String content, Boolean enabled, Integer commentCount, TextChannel textChannel, Member writer) {
        this.content = content;
        this.enabled = enabled;
        this.commentCount = commentCount;
        this.textChannel = textChannel;
        this.writer = writer;
    }

    public static Thread of(String content, Boolean enabled, Integer commentCount, TextChannel textChannel, Member writer) {
        return new Thread(content, enabled, commentCount, textChannel, writer);
    }

    public void delete() {
        this.enabled = false;
    }

    public void modify(ThreadModifyRequest request) {
        this.content = request.content();
    }

    public void upCommentCount() {
        this.commentCount++;
    }
}
