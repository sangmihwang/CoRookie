package com.fourttttty.corookie.plan.domain;

import com.fourttttty.corookie.global.audit.BaseTime;
import com.fourttttty.corookie.project.domain.Project;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "plan_category")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class PlanCategory extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "plan_category_id", nullable = false)
    private Long id;

    @Column(nullable = false, unique = true)
    private String content;

    @Column(nullable = false)
    private String color;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;

    private PlanCategory(String content, String color, Project project) {
        this.content = content;
        this.color = color;
        this.project = project;
    }

    public static PlanCategory of(String content, String color, Project project) {
        return new PlanCategory(content, color, project);
    }

    private PlanCategory(Long id, String content, String color, Project project) {
        this.id = id;
        this.content = content;
        this.color = color;
        this.project = project;
    }

    public static PlanCategory of(Long id, String content, String color, Project project) {
        return new PlanCategory(id, content, color, project);
    }
}
