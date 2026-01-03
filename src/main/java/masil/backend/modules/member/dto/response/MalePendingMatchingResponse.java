package masil.backend.modules.member.dto.response;

import masil.backend.modules.member.entity.Matching;
import masil.backend.modules.member.enums.MatchingStatus;

import java.time.LocalDateTime;

public record MalePendingMatchingResponse(
        Long matchingId,
        Long femaleMemberId,
        String femaleName,
        String femaleEmail,
        Integer height,
        Integer weight,
        String residenceArea,
        String aiSummary,
        MatchingStatus status,
        LocalDateTime createdAt
) {
    public static MalePendingMatchingResponse from(Matching matching) {
        return new MalePendingMatchingResponse(
                matching.getId(),
                matching.getFemaleMember().getId(),
                matching.getFemaleMember().getName(),
                matching.getFemaleMember().getEmail(),
                matching.getFemaleMember().getHeight(),
                matching.getFemaleMember().getWeight(),
                matching.getFemaleMember().getResidenceArea(),
                matching.getFemaleMember().getAiSummary() != null 
                ? matching.getFemaleMember().getAiSummary() 
                : null,  // null 처리
                matching.getStatus(),
                matching.getCreatedAt()
        );
    }
}

