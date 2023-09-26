package com.fourttttty.corookie.videoanalysis.application.repository;

import com.fourttttty.corookie.videoanalysis.domain.SttToken;
import java.util.Optional;

public interface SttTokenRepository {
    SttToken save(SttToken sttToken);
    Optional<SttToken> findById(Long id);
    Boolean existById(Long id);
}
