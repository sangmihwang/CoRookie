package com.fourttttty.corookie.videoanalysis.dto;

import jakarta.validation.constraints.NotBlank;

public record AnalysisRequest(@NotBlank String recordingName) {
}
