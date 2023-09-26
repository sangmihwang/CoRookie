package com.fourttttty.corookie.videoanalysis.application.repository;

import com.fourttttty.corookie.videoanalysis.domain.Analysis;
import com.fourttttty.corookie.videoanalysis.infrastructure.AnalysisJpaRepository;
import com.fourttttty.corookie.videochannel.domain.VideoChannel;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class AnalysisRepositoryImpl implements AnalysisRepository{
    private final AnalysisJpaRepository analysisJpaRepository;

    @Override
    public Analysis save(Analysis analysis) {
        return analysisJpaRepository.save(analysis);
    }

    @Override
    public List<Analysis> findByVideoChannel(VideoChannel videoChannel) {
        return analysisJpaRepository.findAllByVideoChannel(videoChannel);
    }

    @Override
    public Optional<Analysis> findById(Long id) {
        return analysisJpaRepository.findById(id);
    }
}
