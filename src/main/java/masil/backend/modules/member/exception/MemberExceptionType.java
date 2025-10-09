package masil.backend.modules.member.exception;

import masil.backend.global.base.BaseExceptionType;
import org.springframework.http.HttpStatus;

public enum MemberExceptionType implements BaseExceptionType {

    NOT_FOUND_MEMBER(HttpStatus.NOT_FOUND, "회원을 찾을 수 없습니다."),
    NOT_FOUND_LEADER(HttpStatus.NOT_FOUND, "팀장을 찾을 수 없습니다."),
    ALREADY_EXIST_EMAIL(HttpStatus.BAD_REQUEST, "이미 존재하는 이메일입니다."),
    ALREADY_EXIST_STUDENT_ID(HttpStatus.BAD_REQUEST, "이미 존재하는 학번입니다."),
    CANNOT_MATCH_PASSWORD(HttpStatus.BAD_REQUEST, "비밀번호가 틀렸습니다."),
    CANNOT_CHANGE_SAME_PASSWORD(HttpStatus.BAD_REQUEST, "동일한 비밀번호로 변경할 수 없습니다."),
    
    OAUTH2_AUTHENTICATION_FAILED(HttpStatus.UNAUTHORIZED, "OAuth2 인증에 실패했습니다."),
    OAUTH2_USER_INFO_LOAD_FAILED(HttpStatus.BAD_REQUEST, "OAuth2 사용자 정보를 불러오는데 실패했습니다."),
    ;

    private final HttpStatus httpStatus;
    private final String errorMessage;

    MemberExceptionType(final HttpStatus httpStatus, final String errorMessage) {
        this.httpStatus = httpStatus;
        this.errorMessage = errorMessage;
    }

    @Override
    public HttpStatus httpStatus() {
        return httpStatus;
    }

    @Override
    public String errorMessage() {
        return errorMessage;
    }
}
