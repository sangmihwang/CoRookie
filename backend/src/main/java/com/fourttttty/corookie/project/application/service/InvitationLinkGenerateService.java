package com.fourttttty.corookie.project.application.service;

import com.fourttttty.corookie.project.util.Base62Encoder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InvitationLinkGenerateService {

    private final Base62Encoder base62Encoder;

    public String generateInvitationLink(Long id) {
        return base62Encoder.encoding(id);
    }

    public Long decodingInvitationLink(String link) {
        return base62Encoder.decoding(link);
    }
}
