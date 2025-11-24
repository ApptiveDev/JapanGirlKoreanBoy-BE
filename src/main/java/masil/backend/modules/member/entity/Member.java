package masil.backend.modules.member.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import masil.backend.global.base.BaseEntity;
import masil.backend.modules.member.enums.*;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLRestriction("is_deleted = false")
@SQLDelete(sql = "UPDATE member SET is_deleted = true where id = ?")
public class Member extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Local : 일반 회원가입, Google : 구글 로그인
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Provider provider;

    // 구글 로그인 시 발급받은 고유 아이디
    @Column
    private String providerId;

    @Column(nullable = false)
    private Boolean isDeleted;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MemberStatus status;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column
    private String password;

    /** 필수 정보들 **/
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Gender gender;

    @Column
    private Integer height;

    @Column
    private Integer weight;

    @Column
    private String residenceArea;

    @Enumerated(EnumType.STRING)
    @Column
    private SmokingStatus smokingStatus;

    @Enumerated(EnumType.STRING)
    @Column
    private DrinkingFrequency drinkingFrequency;

    @Enumerated(EnumType.STRING)
    @Column
    private Religion religion;

    /** 선택 정보들**/
    @Enumerated(EnumType.STRING)
    @Column
    private Education education;

    @Enumerated(EnumType.STRING)
    @Column
    private Asset asset;

    @Column(columnDefinition = "TEXT")
    private String otherInfo;

    @Column
    private String profileImageUrl;

    @Builder
    private Member(final String name, final String email, final String password, 
                   final Provider provider, final String providerId,
                   final Gender gender, final Integer height, final Integer weight,
                   final String residenceArea, final SmokingStatus smokingStatus,
                   final DrinkingFrequency drinkingFrequency, final Religion religion,
                   final Education education, final Asset asset, final String otherInfo,
                   final String profileImageUrl) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.provider = provider;
        this.providerId = providerId;
        this.gender = gender;
        this.height = height;
        this.weight = weight;
        this.residenceArea = residenceArea;
        this.smokingStatus = smokingStatus;
        this.drinkingFrequency = drinkingFrequency;
        this.religion = religion;
        this.education = education;
        this.asset = asset;
        this.otherInfo = otherInfo;
        this.profileImageUrl = profileImageUrl;
        this.isDeleted = false;
        this.status = MemberStatus.PENDING_APPROVAL;
    }

    public void updatePassword(final String newPassword) {
        this.password = newPassword;
    }

    public boolean isPasswordEqual(final String newPassword) {
        return this.password != null && this.password.equals(newPassword);
    }

    public void approve() {
        this.status = MemberStatus.APPROVED;
    }

    public void blacklist() {
        this.status = MemberStatus.BLACKLISTED;
    }
}
