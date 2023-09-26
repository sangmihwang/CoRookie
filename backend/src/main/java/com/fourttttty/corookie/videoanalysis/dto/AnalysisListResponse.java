package com.fourttttty.corookie.videoanalysis.dto;

import com.fourttttty.corookie.videoanalysis.domain.Analysis;

import java.time.LocalDateTime;

public record AnalysisListResponse(Long id,
                                   String recordName,
                                   LocalDateTime createdAt
) {
    public static AnalysisListResponse from(Analysis analysis) {
        return new AnalysisListResponse(analysis.getId(),
                analysis.getRecordName(),
                analysis.getCreatedAt());
    }
}
