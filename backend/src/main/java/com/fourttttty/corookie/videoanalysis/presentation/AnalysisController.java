package com.fourttttty.corookie.videoanalysis.presentation;

import com.fourttttty.corookie.videoanalysis.application.service.AnalysisService;
import com.fourttttty.corookie.videoanalysis.dto.AnalysisListResponse;
import com.fourttttty.corookie.videoanalysis.dto.AnalysisResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/projects/{projectId}/video-channels/{videoChannelId}/analysis")
public class AnalysisController {
    private final AnalysisService analysisService;

    @PostMapping
    public ResponseEntity<Object> analysisCreate(
        @RequestParam MultipartFile file,
        @RequestParam String recordName,
        @PathVariable Long videoChannelId){
        try {
            return ResponseEntity.ok(analysisService.createAnalysis(file,recordName, videoChannelId));
        }catch (Exception e) {
            return ResponseEntity.status(500).body("An error occurred: " + e.getMessage());
        }
    }

    @GetMapping()
    public ResponseEntity<List<AnalysisListResponse>> analysisList(@PathVariable Long videoChannelId){
        return ResponseEntity.ok(analysisService.findByVideoChannel(videoChannelId));
    }

    @GetMapping("/{analysisId}")
    public ResponseEntity<AnalysisResponse> analysisDetail(@PathVariable Long analysisId){
        return ResponseEntity.ok(analysisService.findById(analysisId));
    }
}
