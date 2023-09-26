package com.fourttttty.corookie.plan.application.repository;

import com.fourttttty.corookie.plan.domain.Plan;
import com.fourttttty.corookie.plan.domain.PlanMember;
import com.fourttttty.corookie.plan.domain.PlanMemberId;
import java.util.List;

public interface PlanMemberRepository {
    List<PlanMember> findByPlanId(Long planId);

    PlanMember save(PlanMember planMember);

    Boolean exists(PlanMemberId planMemberId);

    void delete(PlanMember planMember);
}
