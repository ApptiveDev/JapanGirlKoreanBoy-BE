package masil.backend.modules.member.dto.response;

import masil.backend.modules.member.entity.Member;

public record MyInfoResponse(
        Long memberId,
        String name,
        String email
) {
    public MyInfoResponse(final Member member) {
        this(
                member.getId(),
                member.getName(),
                member.getEmail()
        );
    }
}
