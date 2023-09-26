package com.fourttttty.corookie.project.domain;

import com.fourttttty.corookie.global.audit.BaseTime;
import com.fourttttty.corookie.member.domain.Member;
import com.fourttttty.corookie.textchannel.domain.DefaultChannel;
import com.fourttttty.corookie.textchannel.domain.TextChannel;
import com.fourttttty.corookie.videochannel.domain.DefaultVideoChannel;
import com.fourttttty.corookie.videochannel.domain.VideoChannel;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Table(name = "project")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Project extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "project_id", nullable = false)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private Boolean enabled;

    @Column(nullable = false)
    private String invitationLink;

    @Column(nullable = false)
    private Boolean invitationStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manager_id", nullable = false)
    private Member manager;

    private Project(String name,
                   String description,
                   Boolean enabled,
                   String invitationLink,
                   Boolean invitationStatus,
                   Member manager) {
        this.name = name;
        this.description = description;
        this.enabled = enabled;
        this.invitationLink = invitationLink;
        this.invitationStatus = invitationStatus;
        this.manager = manager;
    }

    public static Project of(String name,
                             String description,
                             Boolean enabled,
                             String invitationLink,
                             Boolean invitationStatus,
                             Member manager) {
        return new Project(name,
                description,
                enabled,
                invitationLink,
                invitationStatus,
                manager);
    }

    public void update(String name,
                       String description,
                       String invitationLink,
                       Boolean invitationStatus) {
        this.name = name;
        this.description = description;
        this.invitationLink = invitationLink;
        this.invitationStatus = invitationStatus;
    }

    public void changeInvitationLink(String invitationLink) {
        this.invitationLink = invitationLink;
    }

    public boolean isManager(Long memberId) {
        return this.manager.equalsId(memberId);
    }

    public boolean isEnabledLink() {
        return this.invitationStatus;
    }

    public void enableLink() {
        this.invitationStatus = true;
    }

    public void disableLink() {
        this.invitationStatus = false;
    }

    public void delete() {
        this.enabled = false;
    }

    public List<TextChannel> createDefaultTextChannels() {
        List<TextChannel> defaultChannels = new ArrayList<>();
        for (DefaultChannel channel : DefaultChannel.values()) {
            TextChannel textChannel = TextChannel.of(channel.getChannelName(), true, false, this);
            textChannel.changeNotDeletableChannel();
            defaultChannels.add(textChannel);
        }

        return defaultChannels;
    }

    public List<VideoChannel> createDefaultVideoChannels() {
        List<VideoChannel> defaultChannels = new ArrayList<>();
        for (DefaultVideoChannel channel : DefaultVideoChannel.values()) {
            String sessionId = UUID.randomUUID().toString();
            VideoChannel videoChannel = VideoChannel.of(channel.getChannelName(), true, false, this, sessionId);
            videoChannel.changeNotDeletableChannel();
            defaultChannels.add(videoChannel);
        }

        return defaultChannels;
    }
}
