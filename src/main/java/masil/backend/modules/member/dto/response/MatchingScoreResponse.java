package masil.backend.modules.member.dto.response;

import masil.backend.modules.member.entity.Member;

public record MatchingScoreResponse(
        Long memberId,
        String name,
        String email,
        Integer height,
        Integer weight,
        String residenceArea,
        Double matchingScore
) {
    public static MatchingScoreResponse from(Member member, Double score) {
        return new MatchingScoreResponse(
                member.getId(),
                member.getName(),
                member.getEmail(),
                member.getHeight(),
                member.getWeight(),
                member.getResidenceArea(),
                score
        );
    }
}