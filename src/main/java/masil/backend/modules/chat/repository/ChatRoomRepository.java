package masil.backend.modules.chat.repository;

import masil.backend.modules.chat.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    
    @Query("SELECT cr FROM ChatRoom cr WHERE cr.isDeleted = false " +
           "AND (cr.member1.id = :memberId OR cr.member2.id = :memberId) " +
           "ORDER BY cr.updatedAt DESC")
    List<ChatRoom> findAllByMemberId(@Param("memberId") Long memberId);
    
    @Query("SELECT cr FROM ChatRoom cr WHERE cr.isDeleted = false " +
           "AND ((cr.member1.id = :member1Id AND cr.member2.id = :member2Id) " +
           "OR (cr.member1.id = :member2Id AND cr.member2.id = :member1Id))")
    Optional<ChatRoom> findByMember1AndMember2(
            @Param("member1Id") Long member1Id,
            @Param("member2Id") Long member2Id
    );
    
    @Query("SELECT cr FROM ChatRoom cr WHERE cr.id = :chatRoomId " +
           "AND cr.isDeleted = false " +
           "AND (cr.member1.id = :memberId OR cr.member2.id = :memberId)")
    Optional<ChatRoom> findByIdAndMemberId(
            @Param("chatRoomId") Long chatRoomId,
            @Param("memberId") Long memberId
    );
    
    Optional<ChatRoom> findByMatchingId(Long matchingId);
}

