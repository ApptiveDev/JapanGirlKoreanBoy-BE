package masil.backend.modules.member.dto.request;

import jakarta.validation.constraints.NotNull;
import masil.backend.modules.member.enums.MemberStatus;

public record SignInRequest(
        @NotNull(message = "가입한 이메일을 입력해주세요.")
        String email,

        @NotNull(message = "비밀번호를 입력해주세요.")
        String password,

        MemberStatus status
) {
}
