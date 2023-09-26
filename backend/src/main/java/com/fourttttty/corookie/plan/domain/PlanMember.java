package com.fourttttty.corookie.plan.domain;

import com.fourttttty.corookie.member.domain.Member;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "plan_member")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PlanMember {
    @EmbeddedId
    private PlanMemberId id;

    private PlanMember(PlanMemberId planMemberId) {this.id = planMemberId; }

    public static PlanMember of(Member member, Plan plan){
        return new PlanMember(PlanMemberId.of(member,plan));
    }
}
