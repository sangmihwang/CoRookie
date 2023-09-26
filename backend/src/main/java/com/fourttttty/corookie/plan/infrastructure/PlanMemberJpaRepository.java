package com.fourttttty.corookie.plan.infrastructure;

import com.fourttttty.corookie.plan.domain.PlanMember;
import com.fourttttty.corookie.plan.domain.PlanMemberId;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlanMemberJpaRepository extends JpaRepository<PlanMember, PlanMemberId> {
    List<PlanMember> findAllByIdPlanId(Long planId);
}
