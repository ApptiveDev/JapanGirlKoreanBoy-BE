package masil.backend.modules.chat.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import masil.backend.global.security.annotation.LoginMember;
import masil.backend.global.security.dto.MemberDetails;
import masil.backend.modules.chat.dto.response.ChatMessageResponse;
import masil.backend.modules.chat.dto.response.ChatRoomResponse;
import masil.backend.modules.chat.service.ChatHighService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/chat")
@RequiredArgsConstructor
public class ChatController {
    
    private final ChatHighService chatHighService;
    
    /**
     * 내 채팅방 목록 조회
     */
    @GetMapping("/rooms")
    public ResponseEntity<List<ChatRoomResponse>> getAllChatRooms(
            @LoginMember MemberDetails memberDetails
    ) {
        List<ChatRoomResponse> response = chatHighService.getAllChatRooms(memberDetails.memberId());
        return ResponseEntity.ok(response);
    }
    
    /**
     * 특정 채팅방 조회
     */
    @GetMapping("/rooms/{chatRoomId}")
    public ResponseEntity<ChatRoomResponse> getChatRoom(
            @LoginMember MemberDetails memberDetails,
            @PathVariable Long chatRoomId
    ) {
        ChatRoomResponse response = chatHighService.getChatRoom(chatRoomId, memberDetails.memberId());
        return ResponseEntity.ok(response);
    }
    
    /**
     * 채팅방 메시지 목록 조회 (페이징)
     */
    @GetMapping("/rooms/{chatRoomId}/messages")
    public ResponseEntity<Page<ChatMessageResponse>> getMessages(
            @LoginMember MemberDetails memberDetails,
            @PathVariable Long chatRoomId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size
    ) {
        Page<ChatMessageResponse> response = chatHighService.getMessages(
                chatRoomId, memberDetails.memberId(), page, size
        );
        return ResponseEntity.ok(response);
    }
    
    /**
     * 채팅방 읽음 처리
     */
    @PutMapping("/rooms/{chatRoomId}/read")
    public ResponseEntity<Void> markChatRoomAsRead(
            @LoginMember MemberDetails memberDetails,
            @PathVariable Long chatRoomId
    ) {
        chatHighService.markChatRoomAsRead(chatRoomId, memberDetails.memberId());
        return ResponseEntity.ok().build();
    }
    
    /**
     * 전체 읽지 않은 메시지 수 조회
     */
    @GetMapping("/unread-count")
    public ResponseEntity<Long> getTotalUnreadCount(
            @LoginMember MemberDetails memberDetails
    ) {
        Long count = chatHighService.getTotalUnreadCount(memberDetails.memberId());
        return ResponseEntity.ok(count);
    }
}

