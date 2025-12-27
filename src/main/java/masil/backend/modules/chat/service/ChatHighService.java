package masil.backend.modules.chat.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import masil.backend.modules.chat.dto.response.ChatMessageResponse;
import masil.backend.modules.chat.dto.response.ChatRoomResponse;
import masil.backend.modules.chat.entity.ChatMessage;
import masil.backend.modules.chat.entity.ChatRoom;
import masil.backend.modules.chat.exception.ChatException;
import masil.backend.modules.chat.exception.ChatExceptionType;
import masil.backend.modules.member.entity.Member;
import masil.backend.modules.member.repository.MemberRepository;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ChatHighService {
    
    private final ChatLowService chatLowService;
    private final MemberRepository memberRepository;
    
    /**
     * 매칭 성사 시 채팅방 자동 생성
     */
    @Transactional
    public ChatRoom createChatRoomFromMatching(Member member1, Member member2, Long matchingId) {
        // 이미 채팅방이 있는지 확인
        ChatRoom existingChatRoom = chatLowService.findChatRoomByMatchingId(matchingId);
        if (existingChatRoom != null && !existingChatRoom.getIsDeleted()) {
            log.info("매칭 ID로 이미 채팅방이 존재함: matchingId={}, chatRoomId={}", matchingId, existingChatRoom.getId());
            return existingChatRoom;
        }
        
        // 새 채팅방 생성
        ChatRoom chatRoom = chatLowService.saveChatRoom(member1, member2, matchingId);
        log.info("매칭 성사로 채팅방 생성 완료: chatRoomId={}, matchingId={}, member1={}, member2={}", 
                chatRoom.getId(), matchingId, member1.getId(), member2.getId());
        
        return chatRoom;
    }
    
    @Transactional(readOnly = true)
    public List<ChatRoomResponse> getAllChatRooms(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new ChatException(ChatExceptionType.MEMBER_NOT_FOUND));
        
        List<ChatRoom> chatRooms = chatLowService.findAllChatRoomsByMemberId(memberId);
        
        return chatRooms.stream()
                .map(chatRoom -> getChatRoomResponse(chatRoom, member))
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public ChatRoomResponse getChatRoom(Long chatRoomId, Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new ChatException(ChatExceptionType.MEMBER_NOT_FOUND));
        
        ChatRoom chatRoom = chatLowService.findChatRoomByIdAndMemberId(chatRoomId, memberId);
        return getChatRoomResponse(chatRoom, member);
    }
    
    @Transactional
    public ChatMessageResponse sendMessage(Long senderId, Long chatRoomId, String content) {
        Member sender = memberRepository.findById(senderId)
                .orElseThrow(() -> new ChatException(ChatExceptionType.MEMBER_NOT_FOUND));
        
        ChatRoom chatRoom = chatLowService.findChatRoomByIdAndMemberId(chatRoomId, senderId);
        
        ChatMessage message = chatLowService.saveMessage(chatRoom, sender, content);
        log.info("메시지 전송 완료: messageId={}, chatRoomId={}, senderId={}", 
                message.getId(), chatRoom.getId(), senderId);
        
        return ChatMessageResponse.of(message);
    }
    
    @Transactional(readOnly = true)
    public Page<ChatMessageResponse> getMessages(Long chatRoomId, Long memberId, int page, int size) {
        ChatRoom chatRoom = chatLowService.findChatRoomByIdAndMemberId(chatRoomId, memberId);
        
        Page<ChatMessage> messages = chatLowService.findMessagesByChatRoomId(chatRoomId, page, size);
        
        // 읽지 않은 메시지 읽음 처리
        List<ChatMessage> unreadMessages = chatLowService.findUnreadMessages(chatRoomId, memberId);
        if (!unreadMessages.isEmpty()) {
            chatLowService.markMessagesAsRead(unreadMessages);
        }
        
        return messages.map(ChatMessageResponse::of);
    }
    
    @Transactional
    public void markChatRoomAsRead(Long chatRoomId, Long memberId) {
        ChatRoom chatRoom = chatLowService.findChatRoomByIdAndMemberId(chatRoomId, memberId);
        
        List<ChatMessage> unreadMessages = chatLowService.findUnreadMessages(chatRoomId, memberId);
        if (!unreadMessages.isEmpty()) {
            chatLowService.markMessagesAsRead(unreadMessages);
            log.info("채팅방 읽음 처리 완료: chatRoomId={}, memberId={}, 읽음 처리된 메시지 수={}", 
                    chatRoomId, memberId, unreadMessages.size());
        }
    }
    
    @Transactional(readOnly = true)
    public Long getTotalUnreadCount(Long memberId) {
        List<ChatMessage> unreadMessages = chatLowService.findAllUnreadMessagesByMemberId(memberId);
        return (long) unreadMessages.size();
    }
    
    public ChatRoom findChatRoomByIdAndMemberId(Long chatRoomId, Long memberId) {
        return chatLowService.findChatRoomByIdAndMemberId(chatRoomId, memberId);
    }
    
    private ChatRoomResponse getChatRoomResponse(ChatRoom chatRoom, Member currentMember) {
        Member otherMember = chatRoom.getOtherMember(currentMember.getId());
        
        // 마지막 메시지 조회
        Page<ChatMessage> lastMessagePage = chatLowService.findMessagesByChatRoomId(chatRoom.getId(), 0, 1);
        String lastMessage = null;
        LocalDateTime lastMessageTime = null;
        if (!lastMessagePage.isEmpty()) {
            ChatMessage lastMsg = lastMessagePage.getContent().get(0);
            lastMessage = lastMsg.getContent();
            lastMessageTime = lastMsg.getCreatedAt();
        }
        
        // 읽지 않은 메시지 수
        Long unreadCount = chatLowService.countUnreadMessages(chatRoom.getId(), currentMember.getId());
        
        return ChatRoomResponse.of(chatRoom, currentMember, lastMessage, lastMessageTime, unreadCount);
    }
}

