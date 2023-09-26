package com.fourttttty.corookie.thread.application.repository;

import com.fourttttty.corookie.config.audit.JpaAuditingConfig;
import com.fourttttty.corookie.member.domain.AuthProvider;
import com.fourttttty.corookie.member.domain.Member;
import com.fourttttty.corookie.member.domain.Oauth2;
import com.fourttttty.corookie.project.domain.Project;
import com.fourttttty.corookie.support.TestConfig;
import com.fourttttty.corookie.textchannel.domain.TextChannel;
import com.fourttttty.corookie.thread.domain.Emoji;
import com.fourttttty.corookie.thread.domain.ThreadEmoji;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import com.fourttttty.corookie.thread.domain.Thread;
import org.junit.platform.commons.logging.Logger;
import org.junit.platform.commons.logging.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.lang.reflect.Executable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
@Import({JpaAuditingConfig.class, TestConfig.class})
class ThreadEmojiRepositoryTest {

    Logger logger = LoggerFactory.getLogger(ThreadEmojiRepositoryTest.class);

    @Autowired
    ThreadEmojiRepository threadEmojiRepository;

    private Member member;
    private Project project;
    private TextChannel textChannel;
    private Thread thread;

    @BeforeEach
    void initObjects() {
        member = Member.of("name", "email", "https://test.com", Oauth2.of(AuthProvider.KAKAO, "account"));
        project = Project.of("memberName",
                "description",
                Boolean.FALSE,
                "http://test.com",
                Boolean.FALSE,
                member);
        textChannel = textChannel.of("channel name", true, true, project);
        thread = Thread.of("content",
                Boolean.TRUE,
                0,
                textChannel,
                member);
    }

    @Test
    @DisplayName("쓰레드 이모지 저장 테스트")
    void save() {
        // given
        ThreadEmoji threadEmoji = ThreadEmoji.of(Emoji.GOOD, 1L, 1L);

        // when
        ThreadEmoji savedThreadEmoji = threadEmojiRepository.save(threadEmoji);

        // then
        assertThat(savedThreadEmoji.getEmoji()).isEqualTo(threadEmoji.getEmoji());
        assertThat(savedThreadEmoji.getMemberId()).isEqualTo(threadEmoji.getMemberId());
        assertThat(savedThreadEmoji.getThreadId()).isEqualTo(threadEmoji.getThreadId());
    }

    @Test
    @DisplayName("쓰레드별 이모지 정보 조회 테스트")
    void findByEmojiAndThread() {
        // given
        Long memberId = 1L;
        Long threadId = 1L;

        ThreadEmoji threadEmoji = ThreadEmoji.of(Emoji.GOOD, memberId, threadId);
        threadEmojiRepository.save(threadEmoji);

        // when
        Optional<ThreadEmoji> foundThreadEmoji = threadEmojiRepository.findByMemberAndEmojiAndThread(Emoji.GOOD, memberId, threadId);

        // then
        assertThat(foundThreadEmoji).isPresent();
        assertThat(foundThreadEmoji.get().getEmoji()).isEqualTo(Emoji.GOOD);
        assertThat(foundThreadEmoji.get().getMemberId()).isEqualTo(memberId);
        assertThat(foundThreadEmoji.get().getThreadId()).isEqualTo(threadId);
    }

    @Test
    @DisplayName("쓰레드별 이모지 갯수 조회 테스트")
    void countByEmojiAndThread() {
        // given
        Long memberId = 1L;
        Long threadId = 1L;

        List<ThreadEmoji> list = List.of(
                ThreadEmoji.of(Emoji.GOOD, memberId, threadId),
                ThreadEmoji.of(Emoji.BAD, memberId, threadId),
                ThreadEmoji.of(Emoji.SMILE, memberId, threadId));

        for (ThreadEmoji threadEmoji : list) {
            threadEmojiRepository.save(threadEmoji);
        }

        // when
        Long count = threadEmojiRepository.countByEmojiAndThread(Emoji.GOOD, threadId);

        // then
        assertThat(count).isEqualTo(1L);
    }


    @Test
    @DisplayName("쓰레드별 / 회원별 이모지 클릭 여부 조회 테스트")
    void existsByEmojiAndMember() {
        // given
        Long threadId = 1L;
        Long memberId = 1L;
        Long otherMemberId = 2L;

        ThreadEmoji threadEmoji1 = ThreadEmoji.of(Emoji.GOOD, memberId, threadId);
        ThreadEmoji threadEmoji2 = ThreadEmoji.of(Emoji.BAD, memberId, threadId);
        ThreadEmoji threadEmoji3 = ThreadEmoji.of(Emoji.GOOD, otherMemberId, threadId);

        threadEmojiRepository.save(threadEmoji1);
        threadEmojiRepository.save(threadEmoji2);
        threadEmojiRepository.save(threadEmoji3);

        // when
        Boolean isClicked1 = threadEmojiRepository.existsByMemberAndEmojiAndThread(Emoji.GOOD, memberId, threadId);
        Boolean isClicked2 = threadEmojiRepository.existsByMemberAndEmojiAndThread(Emoji.GOOD, memberId, threadId);
        Boolean isClicked3 = threadEmojiRepository.existsByMemberAndEmojiAndThread(Emoji.SMILE, memberId, threadId);

        // then
        assertThat(isClicked1).isEqualTo(Boolean.TRUE);
        assertThat(isClicked2).isEqualTo(Boolean.TRUE);
        assertThat(isClicked3).isEqualTo(Boolean.FALSE);
    }

    @Test
    @DisplayName("쓰레드별 이모지 삭제 테스트")
    void delete() {
        // given
        Long threadId = 1L;
        Long memberId = 1L;

        ThreadEmoji threadEmoji = ThreadEmoji.of(Emoji.GOOD, memberId, threadId);
        threadEmojiRepository.save(threadEmoji);
        ThreadEmoji foundThreadEmoji = threadEmojiRepository
                .findByMemberAndEmojiAndThread(Emoji.GOOD, memberId, threadId)
                .orElseThrow(EntityNotFoundException::new);

        // when
        threadEmojiRepository.deleteById(foundThreadEmoji.getId());

        // then
        assertThrows(EntityNotFoundException.class, () -> threadEmojiRepository
                .findByMemberAndEmojiAndThread(Emoji.GOOD, memberId, threadId)
                .orElseThrow(EntityNotFoundException::new));
    }
}