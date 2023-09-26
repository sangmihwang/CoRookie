package com.fourttttty.corookie.issue.infrastructure;

import com.fourttttty.corookie.issue.domain.Issue;
import com.fourttttty.corookie.issue.domain.IssueCategory;
import com.fourttttty.corookie.issue.domain.IssuePriority;
import com.fourttttty.corookie.issue.domain.IssueProgress;
import com.fourttttty.corookie.member.domain.Member;
import com.fourttttty.corookie.project.domain.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IssueJpaRepository extends JpaRepository<Issue, Long> {
    List<Issue> findByProjectId(Long project);
    List<Issue> findByProjectIdAndManagerId(Long projectId, Long managerId);

    List<Issue> findByProjectIdAndTopicContaining(Long projectId, String topic);

    @Query("select i from Issue i where i.project.id = :projectId order by i.priority asc ")
    List<Issue> findAllOrderByPriorityAsc(@Param("projectId") Long projectId);

    @Query("select i from Issue i where i.project.id = :projectId order by i.priority desc ")
    List<Issue> findAllOrderByPriorityDesc(@Param("projectId") Long projectId);

    List<Issue> findByProjectIdAndCategory(Long projectId, IssueCategory category);

    List<Issue> findByProjectIdAndProgress(Long projectId, IssueProgress progress);
}
