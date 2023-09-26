package com.fourttttty.corookie.videoanalysis.domain;

import com.fourttttty.corookie.global.audit.BaseTime;
import com.fourttttty.corookie.videochannel.domain.VideoChannel;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.io.IOException;
import java.io.InputStream;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.web.multipart.MultipartFile;

@Entity
@Table(name = "analysis")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EntityListeners(AuditingEntityListener.class)
public class Analysis extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "analysis_id", nullable = false)
    private Long id;

    @Column(nullable = false)
    private String recordName;

    @Column(nullable = false)
    private String s3URL;

    @Column(nullable = false, columnDefinition = "text")
    private String sttText;

    @Column(nullable = false, columnDefinition = "text")
    private String summarizationText;

    @Column(nullable = false)
    private Boolean enabled;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "video_channel_id")
    private VideoChannel videoChannel;

    private Analysis(String recordName,
        String s3URL,
        String sttText,
        String summarizationText,
        Boolean enabled,
        VideoChannel videoChannel) {
        this.recordName = recordName;
        this.s3URL = s3URL;
        this.sttText = sttText;
        this.summarizationText = summarizationText;
        this.enabled = enabled;
        this.videoChannel = videoChannel;
    }

    public static Analysis of(String recordName,
        String s3URL,
        String sttText,
        String summarizationText,
        Boolean enabled,
        VideoChannel videoChannel) {
        return new Analysis(recordName,s3URL, sttText, summarizationText, enabled, videoChannel);
    }

    public void delete() {
        this.enabled = false;
    }

    public static MultipartBodyBuilder multiPartFileToBodyBuilder(MultipartFile file)
        throws IOException {
        String fileName = file.getOriginalFilename();
        String contentType = file.getContentType();

        InputStream inputStream = file.getInputStream();
        DataBufferFactory bufferFactory = new DefaultDataBufferFactory();
        DataBuffer dataBuffer = bufferFactory.wrap(inputStream.readAllBytes());

        MultipartBodyBuilder bodyBuilder = new MultipartBodyBuilder();
        bodyBuilder.part("file", dataBuffer)
            .filename(fileName)
            .contentType(MediaType.valueOf(contentType));
        bodyBuilder.part("config", "{}");

        return bodyBuilder;
    }

    public static String convertFastRequest(String sttText){
        return "{ \"name\": \"string\", \"text\": \""+
            sttText+
            "\" }";
    }
}
