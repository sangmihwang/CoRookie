package com.fourttttty.corookie.directmessagechannel.application.repository;

import com.fourttttty.corookie.config.audit.JpaAuditingConfig;
import com.fourttttty.corookie.directmessagechannel.domain.DirectMessageChannel;
import com.fourttttty.corookie.member.application.repository.MemberRepository;
import com.fourttttty.corookie.member.domain.AuthProvider;
import com.fourttttty.corookie.member.domain.Member;
import com.fourttttty.corookie.member.domain.Oauth2;
import com.fourttttty.corookie.project.application.repository.ProjectRepository;
import com.fourttttty.corookie.project.domain.Project;
import com.fourttttty.corookie.support.TestConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import({JpaAuditingConfig.class, TestConfig.class})
class DirectMessageChannelRepositoryTest {

    @Autowired
    private TestEntityManager em;
    @Autowired
    private DirectMessageChannelRepository directMessageChannelRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private ProjectRepository projectRepository;

    Member member1 = Member.of("member1", "test@gmail.com", "https://test", Oauth2.of(AuthProvider.KAKAO, "account"));
    Member member2 = Member.of("member2", "test@gmail.com", "https://test", Oauth2.of(AuthProvider.KAKAO, "account"));
    Member member3 = Member.of("member3", "test@gmail.com", "https://test", Oauth2.of(AuthProvider.KAKAO, "account"));

    Project project = Project.of("memberName",
            "description",
            true,
            "http://test.com",
            false,
            member1);

    @BeforeEach
    void initObjects() {
        memberRepository.save(member1);
        memberRepository.save(member2);
        memberRepository.save(member3);
        projectRepository.save(project);
    }

    @Test
    void createdDirectMessageChannel() {
        directMessageChannelRepository.save(DirectMessageChannel.of(true, member1, member1, project));
        directMessageChannelRepository.save(DirectMessageChannel.of(true, member1, member2, project));

        List<DirectMessageChannel> directMessageChannels = directMessageChannelRepository.findByMemberId(member1.getId(), project.getId());

        for (DirectMessageChannel directMessageChannel : directMessageChannels) {
            System.out.println("directMessageChannel.getMember1() = " + directMessageChannel.getMember1().getName());
            System.out.println("directMessageChannel.getMember2() = " + directMessageChannel.getMember2().getName());
        }
    }
}