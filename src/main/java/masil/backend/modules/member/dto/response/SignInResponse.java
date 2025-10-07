package masil.backend.modules.member.dto.response;

import masil.backend.modules.member.entity.Member;

public record SignInResponse(
        Long memberId,
        String name,
        String token
) {
    public SignInResponse(final Member member, final String token) {
        this(
                member.getId(),
                member.getName(),
                token
        );
    }
}
