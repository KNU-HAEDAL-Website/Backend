package com.haedal.haedalweb.service;

import com.haedal.haedalweb.dto.response.PreSignedUrlDTO;
import io.awspring.cloud.s3.S3Operations;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.net.URL;
import java.time.Duration;


@Service
public class S3Service {
    private final S3Operations s3Operations;
    private final String bucketName;

    public S3Service(S3Operations s3Operations, @Value("${spring.cloud.aws.s3.bucket}") String bucketName) {
        this.s3Operations = s3Operations;
        this.bucketName = bucketName;
    }

    public PreSignedUrlDTO getPreSignedUrlDTO(String objectKey) {
        URL url = generatePreSignedPutUrl(objectKey);

        return PreSignedUrlDTO.builder()
                .preSignedUrl(url)
                .imageUrl(objectKey)
                .build();
    }

    public void deleteObject(String key) {
        if (key == null) return;
        s3Operations.deleteObject(bucketName, key);
    }

    public URL generatePreSignedGetUrl(String objectKey) {
        if (objectKey == null) return null;
        return s3Operations.createSignedGetURL(bucketName, objectKey, Duration.ofMinutes(10));
    }

    private URL generatePreSignedPutUrl(String objectKey) {
        return s3Operations.createSignedPutURL(bucketName, objectKey, Duration.ofMinutes(10), null, "image/jpeg");
    }
}
