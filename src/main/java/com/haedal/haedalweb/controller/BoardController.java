package com.haedal.haedalweb.controller;

import com.haedal.haedalweb.service.S3Service;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URL;
import java.util.UUID;

@Tag(name = "게시판 API")
@RequiredArgsConstructor
@RestController
public class BoardController {
    private final S3Service s3Service;

    @Operation(summary = "PreSignedUrl 게시판 대표 이미지 저장용 ")
    @GetMapping("/boards/generate-presigned-url")
    public ResponseEntity<URL> generatePreSignedUrl() {
        String objectKey = "boards/" + UUID.randomUUID().toString();
        URL url = s3Service.generatePreSignedUrl(objectKey);

        return ResponseEntity.ok(url);
    }
}
