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

    @Builder
    private Member(final String name, final String email, final String password, 
                    final Provider provider, final String providerId) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.provider = provider;
        this.providerId = providerId;
        this.isDeleted = false;
    }
        
    public void updatePassword(final String newPassword) {
        this.password = newPassword;
    }
    //OAuth2 로그인 경우 비밀번호 null 가능능
    public boolean isPasswordEqual(final String newPassword) {
        return this.password != null && this.password.equals(newPassword);
    }

    public enum Provider {
        /** 일반 회원가입 */
        LOCAL, 
        /** 구글 OAuth2 로그인 */
        GOOGLE
    }
}
