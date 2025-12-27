package masil.backend.modules.chat.repository;

import masil.backend.modules.chat.entity.ChatMessage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    
    @Query("SELECT cm FROM ChatMessage cm WHERE cm.chatRoom.id = :chatRoomId " +
           "AND cm.isDeleted = false ORDER BY cm.createdAt DESC")
    Page<ChatMessage> findByChatRoomIdOrderByCreatedAtDesc(
            @Param("chatRoomId") Long chatRoomId,
            Pageable pageable
    );
    
    @Query("SELECT COUNT(cm) FROM ChatMessage cm WHERE cm.chatRoom.id = :chatRoomId " +
           "AND cm.sender.id != :memberId AND cm.isRead = false AND cm.isDeleted = false")
    Long countUnreadMessages(@Param("chatRoomId") Long chatRoomId, @Param("memberId") Long memberId);
    
    @Query("SELECT cm FROM ChatMessage cm WHERE cm.chatRoom.id = :chatRoomId " +
           "AND cm.sender.id != :memberId AND cm.isRead = false AND cm.isDeleted = false")
    List<ChatMessage> findUnreadMessages(@Param("chatRoomId") Long chatRoomId, @Param("memberId") Long memberId);
    
    @Query("SELECT cm FROM ChatMessage cm WHERE cm.chatRoom.id IN " +
           "(SELECT cr.id FROM ChatRoom cr WHERE cr.isDeleted = false " +
           "AND (cr.member1.id = :memberId OR cr.member2.id = :memberId)) " +
           "AND cm.sender.id != :memberId AND cm.isRead = false AND cm.isDeleted = false")
    List<ChatMessage> findAllUnreadMessagesByMemberId(@Param("memberId") Long memberId);
}

