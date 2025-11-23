package masil.backend.modules.member.dto.response;

import masil.backend.modules.member.entity.Matching;
import masil.backend.modules.member.entity.Member;

import java.time.LocalDateTime;
import java.util.List;

public record MatchedMemberListResponse(
        Long matchingId,
        Long femaleMemberId,
        String femaleName,
        String femaleEmail,
        List<MatchedMaleInfo> maleMembers,
        LocalDateTime createdAt
) {
    public static MatchedMemberListResponse from(List<Matching> matchings) {
        if (matchings.isEmpty()) {
            return null;
        }
        
        Matching firstMatching = matchings.get(0);
        Member female = firstMatching.getFemaleMember();
        
        List<MatchedMaleInfo> males = matchings.stream()
                .sorted((a, b) -> a.getMatchingOrder().compareTo(b.getMatchingOrder()))
                .map(m -> new MatchedMaleInfo(
                        m.getId(),
                        m.getMaleMember().getId(),
                        m.getMaleMember().getName(),
                        m.getMaleMember().getEmail(),
                        m.getMatchingOrder()
                ))
                .toList();
        
        return new MatchedMemberListResponse(
                firstMatching.getId(),
                female.getId(),
                female.getName(),
                female.getEmail(),
                males,
                firstMatching.getCreatedAt()
        );
    }
    
    public record MatchedMaleInfo(
            Long matchingId,
            Long memberId,
            String name,
            String email,
            Integer order
    ) {}
}