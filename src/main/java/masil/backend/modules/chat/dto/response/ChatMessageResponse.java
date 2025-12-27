package masil.backend.modules.chat.dto.response;

import masil.backend.modules.chat.entity.ChatMessage;

import java.time.LocalDateTime;

public record ChatMessageResponse(
        Long messageId,
        Long senderId,
        String senderName,
        String senderProfileImageUrl,
        String content,
        Boolean isRead,
        LocalDateTime createdAt
) {
    public static ChatMessageResponse of(ChatMessage message) {
        return new ChatMessageResponse(
                message.getId(),
                message.getSender().getId(),
                message.getSender().getName(),
                message.getSender().getProfileImageUrl(),
                message.getContent(),
                message.getIsRead(),
                message.getCreatedAt()
        );
    }
}

