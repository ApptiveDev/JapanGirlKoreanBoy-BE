package masil.backend.modules.chat.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import masil.backend.modules.chat.entity.ChatMessage;
import masil.backend.modules.chat.entity.ChatRoom;
import masil.backend.modules.chat.exception.ChatException;
import masil.backend.modules.chat.exception.ChatExceptionType;
import masil.backend.modules.chat.repository.ChatMessageRepository;
import masil.backend.modules.chat.repository.ChatRoomRepository;
import masil.backend.modules.member.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatLowService {
    
    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;
    
    @Transactional
    public ChatRoom saveChatRoom(Member member1, Member member2, Long matchingId) {
        ChatRoom chatRoom = ChatRoom.builder()
                .member1(member1)
                .member2(member2)
                .matchingId(matchingId)
                .build();
        return chatRoomRepository.save(chatRoom);
    }
    
    @Transactional
    public ChatMessage saveMessage(ChatRoom chatRoom, Member sender, String content) {
        ChatMessage message = ChatMessage.builder()
                .chatRoom(chatRoom)
                .sender(sender)
                .content(content)
                .build();
        return chatMessageRepository.save(message);
    }
    
    public List<ChatRoom> findAllChatRoomsByMemberId(Long memberId) {
        return chatRoomRepository.findAllByMemberId(memberId);
    }
    
    public ChatRoom findChatRoomById(Long chatRoomId) {
        return chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new ChatException(ChatExceptionType.CHAT_ROOM_NOT_FOUND));
    }
    
    public ChatRoom findChatRoomByIdAndMemberId(Long chatRoomId, Long memberId) {
        return chatRoomRepository.findByIdAndMemberId(chatRoomId, memberId)
                .orElseThrow(() -> new ChatException(ChatExceptionType.CHAT_ROOM_ACCESS_DENIED));
    }
    
    public ChatRoom findChatRoomByMembers(Long member1Id, Long member2Id) {
        return chatRoomRepository.findByMember1AndMember2(member1Id, member2Id)
                .orElse(null);
    }
    
    public ChatRoom findChatRoomByMatchingId(Long matchingId) {
        return chatRoomRepository.findByMatchingId(matchingId)
                .orElse(null);
    }
    
    public Page<ChatMessage> findMessagesByChatRoomId(Long chatRoomId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return chatMessageRepository.findByChatRoomIdOrderByCreatedAtDesc(chatRoomId, pageable);
    }
    
    public Long countUnreadMessages(Long chatRoomId, Long memberId) {
        return chatMessageRepository.countUnreadMessages(chatRoomId, memberId);
    }
    
    public List<ChatMessage> findUnreadMessages(Long chatRoomId, Long memberId) {
        return chatMessageRepository.findUnreadMessages(chatRoomId, memberId);
    }
    
    public List<ChatMessage> findAllUnreadMessagesByMemberId(Long memberId) {
        return chatMessageRepository.findAllUnreadMessagesByMemberId(memberId);
    }
    
    @Transactional
    public void markMessagesAsRead(List<ChatMessage> messages) {
        messages.forEach(ChatMessage::markAsRead);
    }
    
    @Transactional
    public void deleteChatRoom(ChatRoom chatRoom) {
        chatRoom.delete();
    }
    
    @Transactional
    public void deleteMessage(ChatMessage message) {
        message.delete();
    }
}

