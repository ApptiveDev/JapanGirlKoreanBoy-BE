package masil.backend.modules.member.dto.response;

public record OAuth2ErrorResponse(
        String message,
        String details
) {}