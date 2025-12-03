package masil.backend.modules.member.dto.response;

import masil.backend.modules.member.entity.Member;
import masil.backend.modules.member.enums.MemberStatus;

import java.time.LocalDateTime;

public record AdminMemberListResponse(
        Long id,
        String name,
        String email,
        MemberStatus status,
        LocalDateTime createdAt,
        String residenceArea
) {
    public static AdminMemberListResponse from(Member member) {
        return new AdminMemberListResponse(
                member.getId(),
                member.getName(),
                member.getEmail(),
                member.getStatus(),
                member.getCreatedAt(),
                member.getResidenceArea()
        );
    }
}