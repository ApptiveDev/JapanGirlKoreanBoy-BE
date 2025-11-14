package masil.backend.modules.images.dto;

import static masil.backend.modules.images.exception.ImageExceptionType.IMAGE_FILE_NAME_ERROR;
import static masil.backend.modules.images.exception.ImageExceptionType.IMAGE_FILE_TYPE_ERROR;

import java.util.UUID;
import masil.backend.modules.images.dto.request.PresignedUrlRequest;
import masil.backend.modules.images.exception.ImageException;

public record PresignedUrlMetadata(
	String uniqueFileName,
	String objectKey,
	String contentType
) {
	public static PresignedUrlMetadata from(PresignedUrlRequest request) {
		if (request.fileName() == null || request.fileName().isBlank()) {
			throw new ImageException(IMAGE_FILE_NAME_ERROR);
		}
		if (request.contentType() == null || request.contentType().isBlank()) {
			throw new ImageException(IMAGE_FILE_TYPE_ERROR);
		}

		String uniqueFileName = UUID.randomUUID() + "_" + request.fileName();
        String objectKey = "images/" + uniqueFileName;
        return new PresignedUrlMetadata(uniqueFileName, objectKey, request.contentType());
	}
}
