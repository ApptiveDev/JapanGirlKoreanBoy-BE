package masil.backend.modules.images.exception;

import masil.backend.global.base.BaseExceptionType;
import org.springframework.http.HttpStatus;

public enum ImageExceptionType implements BaseExceptionType {

    IMAGE_FILE_NAME_ERROR(HttpStatus.BAD_REQUEST, "이미지 파일을 찾을 수 없습니다."),
    IMAGE_FILE_TYPE_ERROR(HttpStatus.BAD_REQUEST, "이미지 파일 형식이 비어있습니다."),
    S3_PRESIGNED_URL_GENERATION_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "S3 프리사인드 URL 생성에 실패했습니다.")
    ;

    private final HttpStatus httpStatus;
    private final String errorMessage;

    ImageExceptionType(final HttpStatus httpStatus, final String errorMessage) {
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
