package masil.backend.modules.member.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import masil.backend.global.base.BaseEntity;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "matching")
public class Matching extends BaseEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "female_member_id", nullable = false)
    private Member femaleMember;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "male_member_id", nullable = false)
    private Member maleMember;
    
    // 매칭 순서 (1, 2, 3 - 같은 여성과 매칭된 남성들의 순서)
    @Column(nullable = false)
    private Integer matchingOrder;
    
    @Builder
    private Matching(Member femaleMember, Member maleMember, Integer matchingOrder) {
        this.femaleMember = femaleMember;
        this.maleMember = maleMember;
        this.matchingOrder = matchingOrder;
    }
    
    public void updateMaleMember(Member newMaleMember) {
        this.maleMember = newMaleMember;
    }
}