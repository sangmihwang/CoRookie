package com.fourttttty.corookie.videoanalysis.application.repository;

import com.fourttttty.corookie.videoanalysis.domain.Analysis;
import com.fourttttty.corookie.videochannel.domain.VideoChannel;
import java.util.List;
import java.util.Optional;

public interface AnalysisRepository {
    Analysis save(Analysis analysis);
    List<Analysis> findByVideoChannel(VideoChannel videoChannel);
    Optional<Analysis> findById(Long id);
}
