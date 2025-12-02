package masil.backend.modules.member.enums;

public enum MatchingStatus {
    PENDING_FEMALE_SELECTION,  // 여성 선택 대기
    PENDING_MALE_ACCEPTANCE,   // 남성 수락 대기
    ACCEPTED,                  // 수락됨 (채팅 시작 가능)
    REJECTED                   // 거절됨
}

