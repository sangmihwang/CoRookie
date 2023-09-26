package com.fourttttty.corookie.comment.domain;

import com.fourttttty.corookie.global.audit.BaseTime;
import com.fourttttty.corookie.member.domain.Member;
import com.fourttttty.corookie.thread.domain.Thread;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "comment")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Comment extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;

    @Column(nullable = false)
    private String content;
    @Column(nullable = false)
    private Boolean enabled;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "thread_id")
    private Thread thread;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member writer;

    private Comment(String content, Boolean enabled, Thread thread, Member writer) {
        this.content = content;
        this.enabled = enabled;
        this.thread = thread;
        this.writer = writer;
    }

    public static Comment of(String content, Boolean enabled, Thread thread, Member writer) {
        return new Comment(content, enabled, thread, writer);
    }

    public void modify(String content) {
        this.content = content;
    }

    public void delete() {
        this.enabled = false;
    }
}
