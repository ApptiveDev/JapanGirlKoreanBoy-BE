package masil.backend.modules.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WebSocketMessage {
    private MessageType type;
    private Long chatRoomId;
    private Long senderId;
    private String senderName;
    private String senderProfileImageUrl;
    private String content;
    private Long messageId;
    private LocalDateTime createdAt;
    
    public enum MessageType {
        CHAT,      // 일반 채팅 메시지
        JOIN,      // 채팅방 입장
        LEAVE,     // 채팅방 퇴장
        TYPING,    // 입력 중
        READ       // 읽음 처리
    }
}

