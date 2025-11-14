package masil.backend.modules.images.dto.request;

public record PresignedUrlRequest(
	String fileName,
	String contentType
) {
}
