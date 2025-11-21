package masil.backend.modules.member.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public record CreateMatchingRequest(
        @NotNull(message = "여성 유저 ID를 입력해주세요.")
        Long femaleMemberId,
        
        @NotNull(message = "남성 유저 ID 목록을 입력해주세요.")
        @Size(min = 3, max = 3, message = "남성 유저는 정확히 3명을 선택해야 합니다.")
        List<Long> maleMemberIds
) {}