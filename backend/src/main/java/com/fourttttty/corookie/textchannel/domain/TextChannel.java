package com.fourttttty.corookie.textchannel.domain;

import com.fourttttty.corookie.global.audit.BaseTime;
import com.fourttttty.corookie.project.domain.Project;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "text_channel")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class TextChannel extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "channel_id", nullable = false)
    private Long id;

    @Column(nullable = false)
    private String channelName;

    @Column(nullable = false)
    private Boolean enabled;

    @Column(nullable = false)
    private Boolean deletable;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    private TextChannel(String channelName, Boolean enabled, Boolean deletable, Project project) {
        this.channelName = channelName;
        this.enabled = enabled;
        this.deletable = deletable;
        this.project = project;
    }

    public static TextChannel of(String channelName,
                                 Boolean enabled,
                                 Boolean deletable,
                                 Project project) {
        return new TextChannel(channelName, enabled, deletable, project);
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
