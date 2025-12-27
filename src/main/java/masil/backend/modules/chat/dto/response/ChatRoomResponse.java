package masil.backend.modules.chat.dto.response;

import masil.backend.modules.chat.entity.ChatRoom;
import masil.backend.modules.member.entity.Member;

import java.time.LocalDateTime;

public record ChatRoomResponse(
        Long chatRoomId,
        Long otherMemberId,
        String otherMemberName,
        String otherMemberProfileImageUrl,
        String lastMessage,
        LocalDateTime lastMessageTime,
        Long unreadCount
) {
    public static ChatRoomResponse of(ChatRoom chatRoom, Member currentMember, String lastMessage, 
                                     LocalDateTime lastMessageTime, Long unreadCount) {
        Member otherMember = chatRoom.getOtherMember(currentMember.getId());
        return new ChatRoomResponse(
                chatRoom.getId(),
                otherMember.getId(),
                otherMember.getName(),
                otherMember.getProfileImageUrl(),
                lastMessage,
                lastMessageTime,
                unreadCount
        );
    }
}

