package com.fourttttty.corookie.issue.application.service;

import com.fourttttty.corookie.issue.application.repository.IssueRepository;
import com.fourttttty.corookie.issue.domain.IssueCategory;
import com.fourttttty.corookie.issue.domain.Issue;
import com.fourttttty.corookie.issue.domain.IssuePriority;
import com.fourttttty.corookie.issue.domain.IssueProgress;
import com.fourttttty.corookie.issue.dto.response.IssueListResponse;
import com.fourttttty.corookie.member.application.repository.MemberRepository;
import com.fourttttty.corookie.member.domain.AuthProvider;
import com.fourttttty.corookie.member.domain.Member;
import com.fourttttty.corookie.member.domain.Oauth2;
import com.fourttttty.corookie.project.application.repository.ProjectRepository;
import com.fourttttty.corookie.project.domain.Project;
import com.fourttttty.corookie.texture.issue.application.repository.FakeIssueRepository;
import com.fourttttty.corookie.texture.member.application.repository.FakeMemberRepository;
import com.fourttttty.corookie.texture.project.application.repository.FakeProjectRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class IssueFilteringServiceTest {

    IssueRepository issueRepository;
    ProjectRepository projectRepository;
    MemberRepository memberRepository;
    IssueFilteringService issueFilteringService;

    private Member member;
    private Project project;

    @BeforeEach
    void initObjects() {
        projectRepository = new FakeProjectRepository();
        memberRepository = new FakeMemberRepository();
        issueRepository = new FakeIssueRepository(projectRepository, memberRepository);
        issueFilteringService = new IssueFilteringService(issueRepository);
        member = Member.of("memberName", "test@gmail.com", "https://test", Oauth2.of(AuthProvider.KAKAO, "account"));
        project = Project.of("memberName", "description", true,
                "http://test.com", false, member);
        memberRepository.save(member);
        projectRepository.save(project);
    }

    @Test
    @DisplayName("특정 관리자의 이슈들를 가져온다")
    void findByManager() {
        // given
        Issue issue = Issue.of("topic",
                "description",
                IssueProgress.TODO,
                IssuePriority.HIGH,
                IssueCategory.BACKEND,
                true,
                project,
                member);
        issueRepository.save(issue);

        // when
        List<IssueListResponse> responses = issueFilteringService.findByManager(1L, 1L);

        // then
        assertThat(responses).hasSize(1);
        assertThat(responses.get(0).topic()).isEqualTo(issue.getTopic());
        assertThat(responses.get(0).progress()).isEqualTo(issue.getProgress());
        assertThat(responses.get(0).priority()).isEqualTo(issue.getPriority());
        assertThat(responses.get(0).memberName()).isEqualTo(issue.getManager().getName());
    }

    @Test
    @DisplayName("토픽으로 필터링하여 이슈들을 가져온다")
    void findByFilteringWithTopic() {
        // given
        Issue issue = Issue.of("topic",
                "description",
                IssueProgress.TODO,
                IssuePriority.HIGH,
                IssueCategory.BACKEND,
                true,
                project,
                member);
        issueRepository.save(issue);

        // when
        List<IssueListResponse> responses = issueFilteringService.findByFilteringWithTopic(1L, "op");

        // then
        assertThat(responses).hasSize(1);
        assertThat(responses.get(0).topic()).isEqualTo(issue.getTopic());
        assertThat(responses.get(0).progress()).isEqualTo(issue.getProgress());
        assertThat(responses.get(0).priority()).isEqualTo(issue.getPriority());
        assertThat(responses.get(0).memberName()).isEqualTo(issue.getManager().getName());
    }

    @Test
    @DisplayName("진행도로 필터링하여 이슈들을 가져온다")
    void findByFilteringWithProgress() {
        // given
        Issue issue = Issue.of("topic",
                "description",
                IssueProgress.TODO,
                IssuePriority.HIGH,
                IssueCategory.BACKEND,
                true,
                project,
                member);
        issueRepository.save(issue);

        // when
        List<IssueListResponse> responses = issueFilteringService.findByFilteringWithProgress(1L, IssueProgress.TODO);

        // then
        assertThat(responses).hasSize(1);
        assertThat(responses.get(0).topic()).isEqualTo(issue.getTopic());
        assertThat(responses.get(0).progress()).isEqualTo(issue.getProgress());
        assertThat(responses.get(0).priority()).isEqualTo(issue.getPriority());
        assertThat(responses.get(0).memberName()).isEqualTo(issue.getManager().getName());
    }

    @Test
    @DisplayName("중요도를 오름차순으로 정렬하여 이슈들을 가져온다")
    void findOrderByPriorityAsc() {
        // given
        Issue issue = Issue.of("topic",
                "description",
                IssueProgress.TODO,
                IssuePriority.HIGH,
                IssueCategory.BACKEND,
                true,
                project,
                member);
        Issue issue2 = Issue.of("topic",
                "description",
                IssueProgress.TODO,
                IssuePriority.NORMAL,
                IssueCategory.BACKEND,
                true,
                project,
                member);
        issueRepository.save(issue);
        issueRepository.save(issue2);

        // when
        List<IssueListResponse> responses = issueFilteringService.findOrderByPriorityAsc(1L);

        // then
        assertThat(responses).hasSize(2);
        assertThat(responses.get(0).priority()).isEqualTo(issue2.getPriority());
        assertThat(responses.get(1).priority()).isEqualTo(issue.getPriority());
    }

    @Test
    @DisplayName("중요도를 내림차순으로 정렬하여 이슈들을 가져온다")
    void findOrderByPriorityDesc() {
        // given
        Issue issue = Issue.of("topic",
                "description",
                IssueProgress.TODO,
                IssuePriority.HIGH,
                IssueCategory.BACKEND,
                true,
                project,
                member);
        Issue issue2 = Issue.of("topic",
                "description",
                IssueProgress.TODO,
                IssuePriority.NORMAL,
                IssueCategory.BACKEND,
                true,
                project,
                member);
        issueRepository.save(issue);
        issueRepository.save(issue2);

        // when
        List<IssueListResponse> responses = issueFilteringService.findOrderByPriorityDesc(1L);

        // then
        assertThat(responses).hasSize(2);
        assertThat(responses.get(0).priority()).isEqualTo(issue.getPriority());
        assertThat(responses.get(1).priority()).isEqualTo(issue2.getPriority());
    }
}