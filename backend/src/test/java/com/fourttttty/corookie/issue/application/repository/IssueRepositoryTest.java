package com.fourttttty.corookie.issue.application.repository;

import com.fourttttty.corookie.config.audit.JpaAuditingConfig;
import com.fourttttty.corookie.issue.domain.IssueCategory;
import com.fourttttty.corookie.issue.domain.Issue;
import com.fourttttty.corookie.issue.domain.IssuePriority;
import com.fourttttty.corookie.issue.domain.IssueProgress;
import com.fourttttty.corookie.member.application.repository.MemberRepository;
import com.fourttttty.corookie.member.domain.AuthProvider;
import com.fourttttty.corookie.member.domain.Member;
import com.fourttttty.corookie.member.domain.Oauth2;
import com.fourttttty.corookie.project.application.repository.ProjectRepository;
import com.fourttttty.corookie.project.domain.Project;
import com.fourttttty.corookie.support.TestConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import({JpaAuditingConfig.class, TestConfig.class})
class IssueRepositoryTest {

    @Autowired
    private TestEntityManager em;
    @Autowired
    private IssueRepository issueRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private ProjectRepository projectRepository;
    Member member = Member.of("memberName", "test@gmail.com", "https://test", Oauth2.of(AuthProvider.KAKAO, "account"));
    Project project = Project.of("memberName",
            "description",
            true,
            "http://test.com",
            false,
            member);


    @BeforeEach
    void setUp() {
        memberRepository.save(member);
        projectRepository.save(project);
    }

    @Test
    @DisplayName("이슈를 저장한다")
    void saveIssue() {
        // given
        Issue issue = Issue.of("topic",
                "description",
                IssueProgress.TODO,
                IssuePriority.HIGH,
                IssueCategory.BACKEND,
                true,
                project,
                member);

        // when
        Issue savedIssue = issueRepository.save(issue);

        // then
        assertThat(savedIssue).isEqualTo(issue);
        assertThat(savedIssue.getId()).isEqualTo(issue.getId());
        assertThat(savedIssue.getTopic()).isEqualTo(issue.getTopic());
        assertThat(savedIssue.getDescription()).isEqualTo(issue.getDescription());
        assertThat(savedIssue.getProgress()).isEqualTo(issue.getProgress());
        assertThat(savedIssue.getPriority()).isEqualTo(issue.getPriority());
        assertThat(savedIssue.getEnabled()).isEqualTo(issue.getEnabled());
    }

    @Test
    @DisplayName("issueId로 이슈를 조회한다")
    void findById() {
        // given
        Long issueId = 1L;
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
        Optional<Issue> findIssue = issueRepository.findById(issueId);

        // then
        assertThat(findIssue).isNotEmpty();
        assertThat(findIssue.get().getTopic()).isEqualTo(issue.getTopic());
        assertThat(findIssue.get().getDescription()).isEqualTo(issue.getDescription());
        assertThat(findIssue.get().getProgress()).isEqualTo(issue.getProgress());
        assertThat(findIssue.get().getPriority()).isEqualTo(issue.getPriority());
        assertThat(findIssue.get().getEnabled()).isEqualTo(issue.getEnabled());
        assertThat(findIssue.get().getProject()).isEqualTo(issue.getProject());
        assertThat(findIssue.get().getManager()).isEqualTo(issue.getManager());
    }

    @Test
    @DisplayName("projectId로 이슈 목록을 조회한다")
    void findByProjectId() {
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
        List<Issue> findIssue = issueRepository.findByProjectId(project.getId());

        // then
        assertThat(findIssue.size()).isEqualTo(1L);
        assertThat(findIssue.get(0).getTopic()).isEqualTo(issue.getTopic());
        assertThat(findIssue.get(0).getDescription()).isEqualTo(issue.getDescription());
        assertThat(findIssue.get(0).getProgress()).isEqualTo(issue.getProgress());
        assertThat(findIssue.get(0).getPriority()).isEqualTo(issue.getPriority());
        assertThat(findIssue.get(0).getEnabled()).isEqualTo(issue.getEnabled());
        assertThat(findIssue.get(0).getProject()).isEqualTo(issue.getProject());
        assertThat(findIssue.get(0).getManager()).isEqualTo(issue.getManager());
    }

    @Test
    @DisplayName("IssueId로 이슈를 삭제한다")
    void deleteById() {
        // given
        Long issueId = 1L;
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
        issueRepository.deleteById(issueId);

        // then
        assertThat(issueRepository.findById(issueId)).isEmpty();
    }

    @Test
    @DisplayName("이슈 관리자로 특정 프로젝트의 이슈들을 조회한다")
    void findByManagerAndProject() {
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
        List<Issue> issues = issueRepository.findByManager(project.getId(), member.getId());

        // then
        assertThat(issues).hasSize(1);
        assertThat(issues).contains(issue);
    }

    @Test
    @DisplayName("이슈 토픽으로 이슈들을 검색한다")
    void findByTopic() {
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
        List<Issue> issues = issueRepository.findLikeTopic(project.getId(), "op");

        // then
        assertThat(issues).hasSize(1);
        assertThat(issues).contains(issue);
    }

    @Test
    @DisplayName("이슈 중요도 오름차순으로 이슈들을 조회한다")
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
        List<Issue> issues = issueRepository.findOrderByPriorityAsc(project.getId());

        // then
        assertThat(issues).hasSize(2);
        assertThat(issues.get(0).getPriority()).isEqualTo(IssuePriority.HIGH);
        assertThat(issues.get(1).getPriority()).isEqualTo(IssuePriority.NORMAL);
    }

    @Test
    @DisplayName("이슈 중요도 내림차순으로 이슈들을 조회한다")
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
        List<Issue> issues = issueRepository.findOrderByPriorityDesc(project.getId());

        // then
        assertThat(issues).hasSize(2);
        assertThat(issues.get(0).getPriority()).isEqualTo(IssuePriority.NORMAL);
        assertThat(issues.get(1).getPriority()).isEqualTo(IssuePriority.HIGH);
    }

    @Test
    @DisplayName("이슈 진행도로 특정 프로젝트의 이슈들을 조회한다")
    void findByProgress() {
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
        List<Issue> issues = issueRepository.findByProgress(project.getId(), IssueProgress.TODO);

        assertThat(issues).hasSize(1);
        assertThat(issues.get(0)).isEqualTo(issue);
    }
}