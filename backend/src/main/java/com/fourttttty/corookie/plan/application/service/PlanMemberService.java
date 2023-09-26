package com.fourttttty.corookie.plan.application.service;

import com.fourttttty.corookie.member.application.repository.MemberRepository;
import com.fourttttty.corookie.plan.application.repository.PlanMemberRepository;
import com.fourttttty.corookie.plan.application.repository.PlanRepository;
import com.fourttttty.corookie.plan.domain.Plan;
import com.fourttttty.corookie.plan.domain.PlanMember;
import com.fourttttty.corookie.plan.dto.request.PlanMemberCreateRequest;
import com.fourttttty.corookie.plan.dto.response.PlanMemberResponse;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PlanMemberService {
    private final PlanMemberRepository planMemberRepository;
    private final MemberRepository memberRepository;
    private final PlanRepository planRepository;

    @Transactional
    public PlanMemberResponse create(Plan plan, PlanMemberCreateRequest request){
        PlanMember planMember = PlanMember.of(memberRepository.findById(request.memberId()).orElseThrow(
            EntityNotFoundException::new),plan);
        return PlanMemberResponse.from(planMember);
    }

    public List<PlanMemberResponse> findAllByPlanId(Long planId){
        return planMemberRepository.findByPlanId(planId).stream()
            .map(PlanMemberResponse::from)
            .toList();
    }

    @Transactional
    public void deletePlanMember(PlanMember planMember){
        planMemberRepository.delete(planMember);
    }
}
