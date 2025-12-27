package masil.backend.modules.chat.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record SendMessageRequest(
        @NotNull(message = "채팅방 ID를 입력해주세요.")
        Long chatRoomId,
        
        @NotBlank(message = "메시지 내용을 입력해주세요.")
        String content
) {}

