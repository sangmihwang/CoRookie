package com.fourttttty.corookie.thread.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "thread_emoji",
        uniqueConstraints = {
                @UniqueConstraint(name = "member_emoji", columnNames = {"emoji", "member_id", "thread_id"})})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ThreadEmoji {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "thread_emoji_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "emoji", nullable = false)
    private Emoji emoji;

    @Column(name = "member_id", nullable = false)
    private Long memberId;

    @Column(name = "thread_id", nullable = false)
    private Long threadId;

    private ThreadEmoji(Emoji emoji, Long memberId, Long threadId) {
        this.emoji = emoji;
        this.memberId = memberId;
        this.threadId = threadId;
    }

    public static ThreadEmoji of(Emoji emoji, Long memberId, Long threadId) {
        return new ThreadEmoji(emoji, memberId, threadId);
    }

}
