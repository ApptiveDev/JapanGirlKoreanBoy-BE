package masil.backend.modules.chat.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import masil.backend.global.security.dto.MemberDetails;
import masil.backend.modules.chat.dto.WebSocketMessage;
import masil.backend.modules.chat.dto.response.ChatMessageResponse;
import masil.backend.modules.chat.entity.ChatMessage;
import masil.backend.modules.chat.entity.ChatRoom;
import masil.backend.modules.chat.service.ChatHighService;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
@RequiredArgsConstructor
public class ChatWebSocketHandler extends TextWebSocketHandler {
    
    private final ChatHighService chatHighService;
    private final ObjectMapper objectMapper;
    
    // 세션 관리: memberId -> WebSocketSession
    private final Map<Long, WebSocketSession> memberSessions = new ConcurrentHashMap<>();
    
    // 세션 관리: sessionId -> memberId
    private final Map<String, Long> sessionToMember = new ConcurrentHashMap<>();
    
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        Long memberId = getMemberIdFromSession(session);
        if (memberId != null) {
            memberSessions.put(memberId, session);
            sessionToMember.put(session.getId(), memberId);
            log.info("WebSocket 연결 성공: memberId={}, sessionId={}", memberId, session.getId());
        } else {
            log.warn("WebSocket 연결 실패: 인증 정보 없음, sessionId={}", session.getId());
            session.close(CloseStatus.BAD_DATA);
        }
    }
    
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        Long memberId = sessionToMember.get(session.getId());
        if (memberId == null) {
            log.warn("인증되지 않은 세션에서 메시지 수신: sessionId={}", session.getId());
            session.close(CloseStatus.BAD_DATA);
            return;
        }
        
        try {
            WebSocketMessage wsMessage = objectMapper.readValue(message.getPayload(), WebSocketMessage.class);
            
            switch (wsMessage.getType()) {
                case CHAT:
                    handleChatMessage(memberId, wsMessage);
                    break;
                case JOIN:
                    handleJoinRoom(memberId, wsMessage.getChatRoomId());
                    break;
                case READ:
                    handleReadMessage(memberId, wsMessage.getChatRoomId());
                    break;
                default:
                    log.warn("알 수 없는 메시지 타입: {}", wsMessage.getType());
            }
        } catch (Exception e) {
            log.error("WebSocket 메시지 처리 중 오류 발생: memberId={}, error={}", memberId, e.getMessage(), e);
            sendErrorMessage(session, "메시지 처리 중 오류가 발생했습니다.");
        }
    }
    
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        Long memberId = sessionToMember.remove(session.getId());
        if (memberId != null) {
            memberSessions.remove(memberId);
            log.info("WebSocket 연결 종료: memberId={}, sessionId={}, status={}", memberId, session.getId(), status);
        }
    }
    
    private void handleChatMessage(Long senderId, WebSocketMessage wsMessage) {
        try {
            // 메시지 저장
            ChatMessageResponse messageResponse = chatHighService.sendMessage(
                    senderId, 
                    wsMessage.getChatRoomId(), 
                    wsMessage.getContent()
            );
            
            // 채팅방 정보 조회
            ChatRoom chatRoom = chatHighService.findChatRoomByIdAndMemberId(
                    wsMessage.getChatRoomId(), 
                    senderId
            );
            
            // 상대방에게 메시지 전송
            Long receiverId = chatRoom.getOtherMember(senderId).getId();
            sendMessageToMember(receiverId, WebSocketMessage.builder()
                    .type(WebSocketMessage.MessageType.CHAT)
                    .chatRoomId(wsMessage.getChatRoomId())
                    .senderId(messageResponse.senderId())
                    .senderName(messageResponse.senderName())
                    .senderProfileImageUrl(messageResponse.senderProfileImageUrl())
                    .content(messageResponse.content())
                    .messageId(messageResponse.messageId())
                    .createdAt(messageResponse.createdAt())
                    .build());
            
            // 발신자에게도 확인 메시지 전송 (메시지 ID 포함)
            sendMessageToMember(senderId, WebSocketMessage.builder()
                    .type(WebSocketMessage.MessageType.CHAT)
                    .chatRoomId(wsMessage.getChatRoomId())
                    .senderId(messageResponse.senderId())
                    .senderName(messageResponse.senderName())
                    .senderProfileImageUrl(messageResponse.senderProfileImageUrl())
                    .content(messageResponse.content())
                    .messageId(messageResponse.messageId())
                    .createdAt(messageResponse.createdAt())
                    .build());
            
        } catch (Exception e) {
            log.error("채팅 메시지 처리 중 오류: senderId={}, chatRoomId={}, error={}", 
                    senderId, wsMessage.getChatRoomId(), e.getMessage(), e);
        }
    }
    
    private void handleJoinRoom(Long memberId, Long chatRoomId) {
        log.info("채팅방 입장: memberId={}, chatRoomId={}", memberId, chatRoomId);
        // 필요시 읽음 처리 등 추가 로직 구현 가능
    }
    
    private void handleReadMessage(Long memberId, Long chatRoomId) {
        try {
            chatHighService.markChatRoomAsRead(chatRoomId, memberId);
            log.info("채팅방 읽음 처리: memberId={}, chatRoomId={}", memberId, chatRoomId);
        } catch (Exception e) {
            log.error("읽음 처리 중 오류: memberId={}, chatRoomId={}, error={}", 
                    memberId, chatRoomId, e.getMessage(), e);
        }
    }
    
    private void sendMessageToMember(Long memberId, WebSocketMessage message) {
        WebSocketSession session = memberSessions.get(memberId);
        if (session != null && session.isOpen()) {
            try {
                String jsonMessage = objectMapper.writeValueAsString(message);
                session.sendMessage(new TextMessage(jsonMessage));
            } catch (IOException e) {
                log.error("메시지 전송 실패: memberId={}, error={}", memberId, e.getMessage(), e);
                memberSessions.remove(memberId);
                sessionToMember.remove(session.getId());
            }
        } else {
            log.debug("회원이 온라인이 아니거나 세션이 닫혀있음: memberId={}", memberId);
        }
    }
    
    private void sendErrorMessage(WebSocketSession session, String errorMessage) {
        try {
            WebSocketMessage error = WebSocketMessage.builder()
                    .type(WebSocketMessage.MessageType.CHAT)
                    .content(errorMessage)
                    .build();
            String jsonMessage = objectMapper.writeValueAsString(error);
            session.sendMessage(new TextMessage(jsonMessage));
        } catch (IOException e) {
            log.error("에러 메시지 전송 실패: sessionId={}, error={}", session.getId(), e.getMessage(), e);
        }
    }
    
    private Long getMemberIdFromSession(WebSocketSession session) {
        // WebSocketHandshakeInterceptor에서 설정한 memberId 가져오기
        Object memberIdObj = session.getAttributes().get("memberId");
        if (memberIdObj instanceof Long) {
            return (Long) memberIdObj;
        }
        
        // MemberDetails에서 가져오기
        Object memberDetailsObj = session.getAttributes().get("memberDetails");
        if (memberDetailsObj instanceof MemberDetails) {
            return ((MemberDetails) memberDetailsObj).memberId();
        }
        
        log.warn("WebSocket 세션에서 memberId를 찾을 수 없음: sessionId={}", session.getId());
        return null;
    }
}

