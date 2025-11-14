package masil.backend.global.error;

import masil.backend.global.base.BaseExceptionType;
import org.springframework.http.HttpStatus;

public enum GeneralExceptionType implements BaseExceptionType {
    FAILED_TO_CONVERT_JSON(HttpStatus.BAD_REQUEST, "리스트를 JSON으로 변환하는데 실패했습니다."),
    ;

    private final HttpStatus httpStatus;
    private final String errorMessage;

    GeneralExceptionType(final HttpStatus httpStatus, final String errorMessage) {
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
