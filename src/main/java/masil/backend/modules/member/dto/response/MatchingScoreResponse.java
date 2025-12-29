package masil.backend.modules.member.dto.response;

import masil.backend.modules.member.entity.Member;
import masil.backend.modules.member.enums.MatchingStatus;

import java.util.List;

public record MatchingScoreResponse(
        Long memberId,
        String name,
        String email,
        Integer height,
        Integer weight,
        String residenceArea,
        Double matchingScore,
        Integer matchingCount             // 현재 매칭된 개수
) {
    public static MatchingScoreResponse from(Member member, Double score, Integer matchingCount, List<String> matchingStatuses) {
        return new MatchingScoreResponse(
                member.getId(),
                member.getName(),
                member.getEmail(),
                member.getHeight(),
                member.getWeight(),
                member.getResidenceArea(),
                score,
                matchingCount
        );
    }
}