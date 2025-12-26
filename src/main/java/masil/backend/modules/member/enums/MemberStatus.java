package masil.backend.modules.member.enums;

public enum MemberStatus {
    INCOMPLETE_PROFILE,  // 프로필 미완성
    PENDING_APPROVAL,    // 승인대기
    APPROVED,            // 승인완료
    CONNECTING,          // 연결중
    CONNECTED,           // 연결됨
    BLACKLISTED          // 블랙 유저
}
