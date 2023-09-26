package com.fourttttty.corookie.videochannel.domain;

import com.fourttttty.corookie.global.audit.BaseTime;
import com.fourttttty.corookie.project.domain.Project;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;


@Entity
@Table(name = "video_channel")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class VideoChannel extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "video_channel_id", nullable = false)
    private Long id;

    @Column(nullable = false)
    private String channelName;

    @Column(nullable = false)
    private Boolean enabled;

    @Column(nullable = false)
    private Boolean deletable;

    @Column(nullable = false)
    private String sessionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    private VideoChannel(String channelName, Boolean enabled, Boolean deletable, Project project, String sessionId) {
        this.channelName = channelName;
        this.enabled = enabled;
        this.deletable = deletable;
        this.project = project;
        this.sessionId = sessionId;
    }

    public static VideoChannel of(String channelName,
                                  Boolean enabled,
                                  Boolean deletable,
                                  Project project,
                                  String sessionId) {
        return new VideoChannel(channelName, enabled, deletable, project, sessionId);
    }

    public void changeNotDeletableChannel() {
        this.deletable = false;
    }

    public void modifyChannelName(String name) {
        this.channelName = name;
    }

    public void deleteChannel() {
        if (this.deletable) this.enabled = false;
    }
}


