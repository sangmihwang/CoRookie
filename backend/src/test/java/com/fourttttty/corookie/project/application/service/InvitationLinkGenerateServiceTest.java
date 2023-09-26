package com.fourttttty.corookie.project.application.service;

import com.fourttttty.corookie.project.util.Base62Encoder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class InvitationLinkGenerateServiceTest {

    private InvitationLinkGenerateService generateService = new InvitationLinkGenerateService(new Base62Encoder());

    @Test
    @DisplayName("Id를 인코딩한다.")
    void encoding() {
        String link = generateService.generateInvitationLink(1L);

        assertThat(link).isEqualTo("B");
    }

    @Test
    @DisplayName("주소를 디코딩한다.")
    void decoding() {
        Long id = generateService.decodingInvitationLink("B");

        assertThat(id).isEqualTo(1L);
    }
}