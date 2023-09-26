package com.fourttttty.corookie.videoanalysis.application.repository;

import com.fourttttty.corookie.videoanalysis.domain.SttToken;
import com.fourttttty.corookie.videoanalysis.infrastructure.SttTokenJpaRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class SttTokenRepositoryImpl implements SttTokenRepository{
    private final SttTokenJpaRepository sttTokenJpaRepository;

    @Override
    public SttToken save(SttToken sttToken) {
        return sttTokenJpaRepository.save(sttToken);
    }

    @Override
    public Optional<SttToken> findById(Long id) {
        return sttTokenJpaRepository.findById(id);
    }

    @Override
    public Boolean existById(Long id) {
        return sttTokenJpaRepository.existsById(id);
    }
}
