package masil.backend.modules.member.dto.response;

import masil.backend.modules.member.entity.Member;
import masil.backend.modules.member.enums.MemberStatus;

public record MyStatusResponse(
        Long memberId,
        String name,
        MemberStatus status
) {
    public MyStatusResponse(final Member member) {
        this(
                member.getId(),
                member.getName(),
                member.getStatus()
        );
    }
}
