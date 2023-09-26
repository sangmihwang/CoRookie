package com.fourttttty.corookie.textchannel.application.repository;

import com.fourttttty.corookie.config.audit.JpaAuditingConfig;
import com.fourttttty.corookie.member.application.repository.MemberRepository;
import com.fourttttty.corookie.member.domain.AuthProvider;
import com.fourttttty.corookie.member.domain.Member;
import com.fourttttty.corookie.member.domain.Oauth2;
import com.fourttttty.corookie.project.application.repository.ProjectRepository;
import com.fourttttty.corookie.project.domain.Project;
import com.fourttttty.corookie.support.TestConfig;
import com.fourttttty.corookie.textchannel.domain.TextChannel;
import com.fourttttty.corookie.textchannel.domain.TextChannelPin;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import({JpaAuditingConfig.class, TestConfig.class})
class TextChannelRepositoryTest {

    @Autowired
    private TestEntityManager em;
    @Autowired
    private TextChannelRepository textChannelRepository;
    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private TextChannelPinRepository textChannelPinRepository;


    Member member = Member.of(
            "memberName",
            "test@gmail.com",
            "https://test",
            Oauth2.of(AuthProvider.KAKAO, "account"));
    Project project = Project.of("memberName",
            "description",
            true,
            "http://test.com",
            false,
            member);
    TextChannel textChannel1 = TextChannel.of("channel1", true, false, project);
    TextChannel textChannel2 = TextChannel.of("channel2", true, false, project);
    TextChannel textChannel3 = TextChannel.of("channel3", true, false, project);
    TextChannel textChannel4 = TextChannel.of("channel4", true, false, project);
    @BeforeEach
    void initObjects() {
        memberRepository.save(member);
        projectRepository.save(project);
        textChannelRepository.save(textChannel1);
        textChannelRepository.save(textChannel2);
        textChannelRepository.save(textChannel3);
        textChannelRepository.save(textChannel4);
        textChannelPinRepository.save(TextChannelPin.of(textChannel2, member));
        textChannelPinRepository.save(TextChannelPin.of(textChannel4, member));
    }

    @Test
    @DisplayName("프로젝트를 고정된 것 우선, 이름 순으로 조회한다.")
    void findByProjectId() {
        List<TextChannel> textChannels = textChannelRepository.findByProjectId(project.getId(), member.getId());

        for (TextChannel textChannel : textChannels) {
            System.out.println("textChannel.getChannelName() = " + textChannel.getChannelName());
        }
    }
}