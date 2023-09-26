package com.fourttttty.corookie.videoanalysis.infrastructure;

import com.fourttttty.corookie.videoanalysis.domain.Analysis;
import com.fourttttty.corookie.videochannel.domain.VideoChannel;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnalysisJpaRepository extends JpaRepository<Analysis, Long> {
    List<Analysis> findAllByVideoChannel(VideoChannel videoChannel);
}
