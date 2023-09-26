package com.fourttttty.corookie.project.dto.response;
import com.fourttttty.corookie.member.domain.Member;
import com.fourttttty.corookie.project.domain.ProjectMember;
import lombok.Builder;

@Builder
public record ProjectMemberResponse(Long memberId,
                                    String memberName,
                                    String memberEmail,
                                    String memberImageUrl,
                                    Boolean isManager) {
    public static ProjectMemberResponse from(ProjectMember projectMember) {
        Member member = projectMember.getId().getMember();
        return new ProjectMemberResponse(member.getId(), member.getName(), member.getEmail(), member.getImageUrl(),
                projectMember.isManager());
    }
}
