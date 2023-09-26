package com.fourttttty.corookie.videoanalysis.application.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fourttttty.corookie.videoanalysis.application.repository.AnalysisRepository;
import com.fourttttty.corookie.videoanalysis.application.repository.SttTokenRepository;
import com.fourttttty.corookie.videoanalysis.domain.Analysis;
import com.fourttttty.corookie.videoanalysis.domain.SttToken;
import com.fourttttty.corookie.videoanalysis.dto.AnalysisListResponse;
import com.fourttttty.corookie.videoanalysis.dto.AnalysisResponse;
import com.fourttttty.corookie.videochannel.application.repository.VideoChannelRepository;
import jakarta.persistence.EntityNotFoundException;
import java.io.IOException;
import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AnalysisService {
    private final AmazonS3Client amazonS3Client;
    private final AnalysisRepository analysisRepository;
    private final SttTokenRepository sttTokenRepository;
    private final VideoChannelRepository videoChannelRepository;
    private final WebClient webClient;

    @Value("${cloud.aws.s3.bucket}")
    public String bucket;

    @Value("${web-client.vito-api.access-key}")
    public String accessKey;

    @Value("${web-client.vito-api.secret-access-key}")
    public String secretAccessKey;

    @Value("${ai-service.domain}")
    private String aiDomain;

    public List<AnalysisListResponse> findByVideoChannel(Long videoChannelId){
        return analysisRepository.findByVideoChannel(
                videoChannelRepository.findById(videoChannelId).orElseThrow(EntityNotFoundException::new)).stream()
                .map(analysis -> AnalysisListResponse.from(analysis))
                .toList();
    }

    public AnalysisResponse findById(Long id){
        return AnalysisResponse.from(analysisRepository.findById(id).orElseThrow(EntityNotFoundException::new));
    }

    @Transactional
    public AnalysisResponse createAnalysis(MultipartFile file, String recordName ,Long videoChannelId)
        throws Exception {
        String s3URL, sttText, summarizedText;

        s3URL = uploadAudio(file);
        sttText = getSttText(getSttId(file));
        summarizedText = summarizeText(sttText);

        Analysis analysis = analysisRepository.save(Analysis.of(
            recordName,
            s3URL,
            sttText,
            summarizedText,
            true,
            videoChannelRepository.findById(videoChannelId)
                .orElseThrow(EntityNotFoundException::new)
        ));

        return AnalysisResponse.from(analysis);
    }


    public String uploadAudio(MultipartFile file) throws IOException {
        String fileName = file.getOriginalFilename();

        String fileUrl = "https://s3.ap-northeast-2.amazonaws.com/" + bucket + "/test" + fileName;
        ObjectMetadata metaData = new ObjectMetadata();
        metaData.setContentType(file.getContentType());
        metaData.setContentLength(file.getSize());

        amazonS3Client.putObject(bucket, fileName, file.getInputStream(), metaData);
        return fileUrl;
    }


    public String getSttId(MultipartFile file) throws IOException {
        SttToken sttToken;
        try {
            sttToken = (sttTokenRepository.findById(1L)
                .orElseThrow(EntityNotFoundException::new));
        } catch (EntityNotFoundException e) {
            sttToken = sttTokenRepository.save(SttToken.of(getToken()));
        }

        MultipartBodyBuilder bodyBuilder = Analysis.multiPartFileToBodyBuilder(file);

        String initResponse = webClient.post()
            .uri("transcribe")
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + sttToken.getToken())
            .contentType(MediaType.MULTIPART_FORM_DATA)
            .body(BodyInserters.fromMultipartData(bodyBuilder.build()))
            .exchangeToMono(clientResponse -> clientResponse.bodyToMono(String.class))
            .block();

        return jsonParsing(initResponse, "id");
    }

    public String getToken() throws JsonProcessingException {
        String response = webClient.post()
            .uri("authenticate")
            .header("accept", "application/json")
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .bodyValue(
                new String("client_id=" + accessKey + "&client_secret=" + secretAccessKey))
            .retrieve()
            .bodyToMono(String.class)
            .block();

        return jsonParsing(response, "access_token");
    }

    public String summarizeText(String sttText) throws JsonProcessingException {
        String response = webClient.post()
            .uri(aiDomain + "/summarizations/")
            .contentType(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue(Analysis.convertFastRequest(sttText)))
            .retrieve()
            .bodyToMono(String.class)
            .block();

        return jsonParsing(response, "summarization_text");
    }

    public String jsonParsing(String response, String keyValue) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonResponse = objectMapper.readTree(response);

        return jsonResponse.get(keyValue).asText();
    }

    public String getSttText(String transcribeId) throws Exception {
        SttToken sttToken;
        String sttText = new String();
        while (true) {
            try {
                sttToken = (sttTokenRepository.findById(1L)
                    .orElseThrow(EntityNotFoundException::new));
            } catch (EntityNotFoundException e) {
                sttToken = sttTokenRepository.save(SttToken.of(getToken()));
            }

            String response = webClient.get()
                .uri("/transcribe/" + transcribeId)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + sttToken.getToken())
                .retrieve()
                .bodyToMono(String.class)
                .block();

            String responseStatus = jsonParsing(response, "status");

            if (responseStatus.equals("completed")) {
                try {
                    ObjectMapper objectMapper = new ObjectMapper();
                    JsonNode jsonNode = objectMapper.readTree(response);
                    JsonNode utterancesNode = jsonNode.get("results").get("utterances");

                    if (utterancesNode.isArray()) {
                        for (JsonNode utteranceNode : utterancesNode) {
                            if (!utteranceNode.get("msg").isNull()) {
                                sttText += utteranceNode.get("msg").asText();
                            }
                        }
                    }
                    return sttText;
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new Exception("STT Parsing Error");
                }
            } else if (responseStatus.equals("failed")) {
                throw new Exception("STT failed");
            }

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
