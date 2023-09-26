package com.fourttttty.corookie.plan.domain;

import com.fourttttty.corookie.member.domain.Member;
import jakarta.persistence.*;

import java.io.Serializable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@EqualsAndHashCode
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PlanMemberId implements Serializable {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="member_id",nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="plan_id",nullable = false)
    private Plan plan;

    private PlanMemberId(Member member, Plan plan) {
        this.member = member;
        this.plan = plan;
    }
    public static PlanMemberId of(Member member, Plan plan) { return new PlanMemberId(member, plan); }
}
