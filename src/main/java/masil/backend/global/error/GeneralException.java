package masil.backend.global.error;


import masil.backend.global.base.BaseException;
import masil.backend.global.base.BaseExceptionType;
public class GeneralException extends BaseException {

    private final GeneralExceptionType exceptionType;

    public GeneralException(final GeneralExceptionType exceptionType) {
        super(exceptionType.errorMessage());
        this.exceptionType = exceptionType;
    }

    public GeneralException(final GeneralExceptionType exceptionType, final String message) {
        super(message);
        this.exceptionType = exceptionType;
    }

    @Override
    public BaseExceptionType exceptionType() {
        return exceptionType;
    }
}
