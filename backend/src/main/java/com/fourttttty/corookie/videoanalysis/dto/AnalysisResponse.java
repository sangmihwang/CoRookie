package com.fourttttty.corookie.videoanalysis.dto;

import com.fourttttty.corookie.videoanalysis.domain.Analysis;

import java.time.LocalDateTime;

public record AnalysisResponse(Long id,
                               String recordName,
                               String s3URL,
                               String sttText,
                               String summarizationText,
                               LocalDateTime createdAt) {

    public static AnalysisResponse from(Analysis analysis) {
        return new AnalysisResponse(analysis.getId(),
                analysis.getRecordName(),
                analysis.getS3URL(),
                analysis.getSttText(),
                analysis.getSummarizationText(),
                analysis.getCreatedAt());
    }
}
