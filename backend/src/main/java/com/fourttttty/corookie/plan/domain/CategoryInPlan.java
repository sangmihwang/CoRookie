package com.fourttttty.corookie.plan.domain;

import jakarta.persistence.*;

import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "category_in_plan")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CategoryInPlan {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plan_id", nullable = false)
    private Plan plan;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plan_category_id", nullable = false)
    private PlanCategory planCategory;

    private CategoryInPlan(Plan plan, PlanCategory planCategory) {
        this.plan = plan;
        this.planCategory = planCategory;
    }

    public static CategoryInPlan of(Plan plan, PlanCategory planCategory) {
        return new CategoryInPlan(plan, planCategory);
    }
}
