package masil.backend.modules.chat.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import masil.backend.global.base.BaseEntity;
import masil.backend.modules.member.entity.Member;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "chat_room", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"member1_id", "member2_id"})
})
public class ChatRoom extends BaseEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member1_id", nullable = false)
    private Member member1;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member2_id", nullable = false)
    private Member member2;
    
    @Column(nullable = false)
    private Boolean isDeleted = false;
    
    @Column(name = "matching_id")
    private Long matchingId; // 매칭 ID (매칭 성사로 생성된 채팅방인 경우)
    
    @Builder
    private ChatRoom(Member member1, Member member2, Long matchingId) {
        this.member1 = member1;
        this.member2 = member2;
        this.isDeleted = false;
        this.matchingId = matchingId;
    }
    
    public void delete() {
        this.isDeleted = true;
    }
    
    public boolean isParticipant(Long memberId) {
        return member1.getId().equals(memberId) || member2.getId().equals(memberId);
    }
    
    public Member getOtherMember(Long memberId) {
        if (member1.getId().equals(memberId)) {
            return member2;
        } else if (member2.getId().equals(memberId)) {
            return member1;
        }
        throw new IllegalArgumentException("해당 회원은 이 채팅방의 참여자가 아닙니다.");
    }
}

