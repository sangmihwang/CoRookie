package com.fourttttty.corookie.project.domain;

import com.fourttttty.corookie.member.domain.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.parameters.P;

@Entity
@Table(name = "project_member")
@Getter
@NoArgsConstructor
public class ProjectMember {
    @EmbeddedId
    private ProjectMemberId id;

    public ProjectMember(ProjectMemberId id) {
        this.id = id;
    }

    public static ProjectMember of(Project project, Member member) {
        return new ProjectMember(new ProjectMemberId(project, member));
    }

    public boolean isManager() {
        return id.getProject().isManager(id.getMember().getId());
    }
}
