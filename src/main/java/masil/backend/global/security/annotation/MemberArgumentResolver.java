package masil.backend.global.security.annotation;

import lombok.RequiredArgsConstructor;
import masil.backend.global.security.dto.MemberDetails;
import org.springframework.core.MethodParameter;
import org.springframework.lang.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
@RequiredArgsConstructor
public class MemberArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(@NonNull final MethodParameter parameter) {
        return parameter.hasParameterAnnotation(LoginMember.class)
                && MemberDetails.class.isAssignableFrom(parameter.getParameterType());
    }

    @Override
    public Object resolveArgument(@NonNull final MethodParameter parameter,
                                  final ModelAndViewContainer mavContainer,
                                  @NonNull final NativeWebRequest webRequest,
                                  final WebDataBinderFactory binderFactory) {

        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof MemberDetails) {
            return authentication.getPrincipal(); // MemberDetails 반환
        }

        return null;
    }
}
