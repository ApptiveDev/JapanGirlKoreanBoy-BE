package masil.backend.modules.member.dto.request;

import jakarta.validation.constraints.NotNull;
import masil.backend.modules.member.enums.MemberStatus;

public record ChangeMemberStatusRequest(
        @NotNull(message = "상태를 선택해주세요.")
        MemberStatus status
) {}