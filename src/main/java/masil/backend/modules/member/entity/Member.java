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

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;
    
    //OAuth2의 경우 null 가능
    @Column
    private String password;

    @Column(nullable = false)
    private Boolean isDeleted;

    //Local: 일반 회원가입, Google:구글 로그인
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Provider provider;

    //구글 로그인 시 발급받은 고유 아이디
    @Column
    private String providerId;

// ===== 회원가입 시 필요한 필수 정보 =====
    
    /** 성별 (한국인 남성, 일본인 여성) */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Gender gender;

    /** 키 (cm) */
    @Column
    private Integer height;

    /** 몸무게 (kg) */
    @Column
    private Integer weight;

    /** 거주 지역 */
    @Column
    private String residenceArea;

    /** 흡연 유무 */
    @Enumerated(EnumType.STRING)
    @Column
    private SmokingStatus smokingStatus;

    /** 음주 빈도 */
    @Enumerated(EnumType.STRING)
    @Column
    private DrinkingFrequency drinkingFrequency;

    /** 종교 */
    @Enumerated(EnumType.STRING)
    @Column
    private Religion religion;

    // ===== 선택 정보 =====

    /** 학력 (인증) */
    @Enumerated(EnumType.STRING)
    @Column
    private Education education;

    /** 재산 (인증) */
    @Enumerated(EnumType.STRING)
    @Column
    private Asset asset;

    /** 기타 정보 */
    @Column(columnDefinition = "TEXT")
    private String otherInfo;

    /** 프로필 사진 URL */
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
    }

        
    public void updatePassword(final String newPassword) {
        this.password = newPassword;
    }
    //OAuth2 로그인 경우 비밀번호 null 가능능
    public boolean isPasswordEqual(final String newPassword) {
        return this.password != null && this.password.equals(newPassword);
    }

}
