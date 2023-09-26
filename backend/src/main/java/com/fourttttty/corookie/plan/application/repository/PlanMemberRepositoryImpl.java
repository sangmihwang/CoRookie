package com.fourttttty.corookie.plan.application.repository;


import com.fourttttty.corookie.plan.domain.PlanMember;
import com.fourttttty.corookie.plan.domain.PlanMemberId;
import com.fourttttty.corookie.plan.infrastructure.PlanMemberJpaRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PlanMemberRepositoryImpl implements PlanMemberRepository{

    private final PlanMemberJpaRepository planMemberJpaRepository;

    @Override
    public List<PlanMember> findByPlanId(Long planId) {
        return planMemberJpaRepository.findAllByIdPlanId(planId);
    }

    @Override
    public PlanMember save(PlanMember planMember) {
        return planMemberJpaRepository.save(planMember);
    }

    @Override
    public Boolean exists(PlanMemberId planMemberId) {
        return planMemberJpaRepository.existsById(planMemberId);
    }

    @Override
    public void delete(PlanMember planMember) {
        planMemberJpaRepository.delete(planMember);
    }
}
