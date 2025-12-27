package masil.backend.modules.adminMember.dto.request;

import jakarta.validation.constraints.NotBlank;

public record AdminMemberLoginRequest(
        @NotBlank(message = "관리자 ID를 입력해주세요.")
        String adminId,

        @NotBlank(message = "비밀번호를 입력해주세요.")
        String password
) {}
