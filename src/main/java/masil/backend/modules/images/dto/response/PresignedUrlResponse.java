package masil.backend.modules.images.dto.response;

public record PresignedUrlResponse(
	String fileName,
	String presignedUrl,
	String contentType
) {
}
