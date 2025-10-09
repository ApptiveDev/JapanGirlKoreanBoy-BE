package masil.backend.modules.member.exception;


import masil.backend.global.base.BaseException;
import masil.backend.global.base.BaseExceptionType;

public class MemberException extends BaseException {

    private final MemberExceptionType exceptionType;

    public MemberException(final MemberExceptionType exceptionType) {
        super(exceptionType.errorMessage());
        this.exceptionType = exceptionType;
    }

    public MemberException(final MemberExceptionType exceptionType, final String message) {
        super(message);
        this.exceptionType = exceptionType;
    }

    @Override
    public BaseExceptionType exceptionType() {
        return exceptionType;
    }
}
