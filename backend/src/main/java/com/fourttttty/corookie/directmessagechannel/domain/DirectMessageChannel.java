package com.fourttttty.corookie.directmessagechannel.domain;

import com.fourttttty.corookie.global.audit.BaseTime;
import com.fourttttty.corookie.member.domain.Member;
import com.fourttttty.corookie.project.domain.Project;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Getter
@Table(name = "direct_message_channel",
        uniqueConstraints = {
                @UniqueConstraint(name = "directMessageMembers", columnNames = {"project_id", "member1_id", "member2_id"})})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class DirectMessageChannel extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Boolean enabled;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member1_id")
    private Member member1;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member2_id")
    private Member member2;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;

    private DirectMessageChannel(Boolean enabled, Member member1, Member member2, Project project) {
        this.enabled = enabled;
        this.member1 = member1;
        this.member2 = member2;
        this.project = project;
    }

    public static DirectMessageChannel of(Boolean enabled, Member member1, Member member2, Project project) {
        if (member1.getId() < member2.getId()) {
            return new DirectMessageChannel(enabled, member1, member2, project);
        }
        return new DirectMessageChannel(enabled, member2, member1, project);
    }

    public void delete() {
        this.enabled = false;
    }
}
