package masil.backend.modules.images.service;

import static masil.backend.modules.images.exception.ImageExceptionType.S3_PRESIGNED_URL_GENERATION_ERROR;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import masil.backend.global.config.AwsProperties;
import masil.backend.modules.images.dto.PresignedUrlMetadata;
import masil.backend.modules.images.dto.response.PresignedUrlResponse;
import masil.backend.modules.images.exception.ImageException;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;

@Service
@RequiredArgsConstructor
@Slf4j
public class ImageService {
    private final AwsProperties awsProperties;
    private final S3Presigner s3Presigner;

    public PresignedUrlResponse generatePresignedUrl(PresignedUrlMetadata metadata) {
        try {
            PutObjectRequest objectRequest = PutObjectRequest.builder()
                    .bucket(awsProperties.getBucket())
                    .key(metadata.objectKey())
                    .contentType(metadata.contentType())
                    .build();

            PresignedPutObjectRequest presignedRequest = s3Presigner.presignPutObject(req -> req
                    .signatureDuration(Duration.ofMinutes(5)) // 만료 시간 5분
                    .putObjectRequest(objectRequest));

            return new PresignedUrlResponse(
                    metadata.uniqueFileName(),
                    presignedRequest.url().toString(),
                    metadata.contentType()
            );
        } catch (Exception e) {
            log.error("S3 Presigned URL 생성 중 오류 발생 - 버킷: {}, 원인: {}", awsProperties.getBucket(), e.getMessage());
            throw new ImageException(S3_PRESIGNED_URL_GENERATION_ERROR);
        }
    }

    public List<PresignedUrlResponse> generatePresignedUrlList(List<PresignedUrlMetadata> metadataList) {
        List<PresignedUrlResponse> responseList = new ArrayList<>();
        for (PresignedUrlMetadata metadata : metadataList) {
            responseList.add(generatePresignedUrl(metadata));
        }
        return responseList;
    }
}
