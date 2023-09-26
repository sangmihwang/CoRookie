package com.fourttttty.corookie.plan.dto.response;

import com.fourttttty.corookie.member.domain.Member;
import com.fourttttty.corookie.plan.domain.PlanMember;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PlanMemberResponse(Long memberId,
                                 String memberName) {
    public static PlanMemberResponse from(PlanMember planMember){
        Member member = planMember.getId().getMember();
        return new PlanMemberResponse(member.getId(), member.getName());
    }
}
