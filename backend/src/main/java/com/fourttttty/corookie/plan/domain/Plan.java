package com.fourttttty.corookie.plan.domain;

import com.fourttttty.corookie.global.audit.BaseTime;
import com.fourttttty.corookie.project.domain.Project;
import jakarta.persistence.*;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "plan")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Plan extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "plan_id", nullable = false)
    private Long id;

    @Column(nullable = false)
    private String planName;

    @Column(columnDefinition = "text")
    private String description;

    @Column(nullable = false)
    private LocalDateTime planStart;

    @Column(nullable = false)
    private LocalDateTime planEnd;

    @Column(nullable = false)
    private Boolean enabled;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;

    private Plan(String planName,
        String description,
        LocalDateTime planStart,
        LocalDateTime planEnd,
        Boolean enabled,
        Project project) {
        this.planName = planName;
        this.description = description;
        this.planStart = planStart;
        this.planEnd = planEnd;
        this.enabled = enabled;
        this.project = project;
    }

    public static Plan of(String planName,
        String description,
        LocalDateTime planStart,
        LocalDateTime planEnd,
        Boolean enabled,
        Project project) {
        return new Plan(planName,
            description,
            planStart,
            planEnd,
            enabled,
            project);
    }

    private Plan(
        Long id,
        String planName,
        String description,
        LocalDateTime planStart,
        LocalDateTime planEnd,
        Boolean enabled,
        Project project) {
        this.id = id;
        this.planName = planName;
        this.description = description;
        this.planStart = planStart;
        this.planEnd = planEnd;
        this.enabled = enabled;
        this.project = project;
    }

    public static Plan of(Long id,
                          String planName,
                          String description,
                          LocalDateTime planStart,
                          LocalDateTime planEnd,
                          Boolean enabled,
                          Project project) {
        return new Plan(id,
            planName,
            description,
            planStart,
            planEnd,
            enabled,
            project);
    }

    public void update(String planName,
                       String description,
                       LocalDateTime planStart,
                       LocalDateTime planEnd) {
        this.planName = planName;
        this.description = description;
        this.planStart = planStart;
        this.planEnd = planEnd;
    }

    public void delete() {
        this.enabled = false;
    }
}
