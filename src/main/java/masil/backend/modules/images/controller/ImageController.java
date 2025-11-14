package masil.backend.modules.images.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import masil.backend.modules.images.dto.PresignedUrlMetadata;
import masil.backend.modules.images.dto.request.PresignedUrlRequest;
import masil.backend.modules.images.dto.response.PresignedUrlResponse;
import masil.backend.modules.images.service.ImageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/image")
@RequiredArgsConstructor
@Slf4j
public class ImageController {
    private final ImageService imageService;

    @PostMapping("/presigned-url")
    public ResponseEntity<PresignedUrlResponse> getPresignedUrl(
            @RequestBody PresignedUrlRequest request
    ) {
        PresignedUrlMetadata metadata = PresignedUrlMetadata.from(request);
        PresignedUrlResponse response = imageService.generatePresignedUrl(metadata);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/presigned-url/list")
    public ResponseEntity<List<PresignedUrlResponse>> getPresignedUrls(
            @RequestBody List<PresignedUrlRequest> requests
    ) {
        List<PresignedUrlMetadata> metadataList = requests.stream()
                .map(req -> PresignedUrlMetadata.from(req))
                .toList();

        List<PresignedUrlResponse> responseList = imageService.generatePresignedUrlList(metadataList);

        return ResponseEntity.ok(responseList);
    }
}
