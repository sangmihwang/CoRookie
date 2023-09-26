package com.fourttttty.corookie.issue.domain;

import com.fourttttty.corookie.global.audit.BaseTime;
import com.fourttttty.corookie.issue.util.IssuePriorityConverter;
import com.fourttttty.corookie.member.domain.Member;
import com.fourttttty.corookie.project.domain.Project;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Getter
@Table(name = "issue")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Issue extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "issue_id", nullable = false)
    private Long id;

    @Column(nullable = false)
    private String topic;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private IssueProgress progress;

    @Column(nullable = false)
    @Convert(converter = IssuePriorityConverter.class, attributeName = "priority")
    private IssuePriority priority;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private IssueCategory category;

    @Column(nullable = false)
    private Boolean enabled;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manager_id", nullable = false)
    private Member manager;

    private Issue(String topic,
                 String description,
                 IssueProgress progress,
                 IssuePriority priority,
                 IssueCategory category,
                 Boolean enabled,
                 Project project,
                 Member manager) {
        this.topic = topic;
        this.description = description;
        this.progress = progress;
        this.priority = priority;
        this.category = category;
        this.enabled = enabled;
        this.project = project;
        this.manager = manager;
    }

    public static Issue of(String topic,
                           String description,
                           IssueProgress progress,
                           IssuePriority priority,
                           IssueCategory category,
                           Boolean enabled,
                           Project project,
                           Member manager) {
        return new Issue(topic,
                description,
                progress,
                priority,
                category,
                enabled,
                project,
                manager);
    }

    public void changeTopic(String topic) {
        this.topic = topic;
    }

    public void changeDescription(String description) {
        this.description = description;
    }

    public void changeManager(Member manager) {
        this.manager = manager;
    }

    public void changePriority(IssuePriority priority) {
        this.priority = priority;
    }

    public void changeCategory(IssueCategory category) {
        this.category = category;
    }

    public void changeProgress(IssueProgress progress) {
        this.progress = progress;
    }

    public void delete() {
        this.enabled = false;
    }
}
