package masil.backend.modules.member.dto.response;

import masil.backend.modules.member.entity.Matching;
import masil.backend.modules.member.enums.MatchingStatus;

import java.util.List;

public record FemaleMatchingListResponse(
        Long matchingId,
        Long maleMemberId,
        String maleName,
        String maleEmail,
        Integer height,
        Integer weight,
        String residenceArea,
        Integer matchingOrder,
        MatchingStatus status
) {
    public static FemaleMatchingListResponse from(Matching matching) {
        return new FemaleMatchingListResponse(
                matching.getId(),
                matching.getMaleMember().getId(),
                matching.getMaleMember().getName(),
                matching.getMaleMember().getEmail(),
                matching.getMaleMember().getHeight(),
                matching.getMaleMember().getWeight(),
                matching.getMaleMember().getResidenceArea(),
                matching.getMatchingOrder(),
                matching.getStatus()
        );
    }
    
    public static List<FemaleMatchingListResponse> fromList(List<Matching> matchings) {
        return matchings.stream()
                .map(FemaleMatchingListResponse::from)
                .toList();
    }
}

