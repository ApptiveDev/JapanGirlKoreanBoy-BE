package masil.backend.modules.images.exception;
import masil.backend.global.base.BaseException;
import masil.backend.global.base.BaseExceptionType;

public class ImageException extends BaseException {
    private final ImageExceptionType exceptionType;

    public ImageException(final ImageExceptionType exceptionType) {
        super(exceptionType.errorMessage());
        this.exceptionType = exceptionType;
    }

    public ImageException(final ImageExceptionType exceptionType, final String message) {
        super(message);
        this.exceptionType = exceptionType;
    }

    @Override
    public BaseExceptionType exceptionType() {
        return exceptionType;
    }
}
