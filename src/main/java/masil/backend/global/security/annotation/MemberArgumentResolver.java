package masil.backend.global.security.annotation;

import lombok.RequiredArgsConstructor;
import masil.backend.global.security.dto.MemberDetails;
import masil.backend.modules.member.entity.Member;
import masil.backend.modules.member.repository.MemberRepository;
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

    private final MemberRepository memberRepository;

    @Override
    public boolean supportsParameter(@NonNull final MethodParameter parameter) {
        return parameter.hasParameterAnnotation(LoginMember.class)
                && Member.class.isAssignableFrom(parameter.getParameterType());
    }

    @Override
    public Object resolveArgument(@NonNull final MethodParameter parameter,
                                  @NonNull final ModelAndViewContainer mavContainer,
                                  @NonNull final NativeWebRequest webRequest,
                                  @NonNull final WebDataBinderFactory binderFactory) {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null
                && authentication.getPrincipal() instanceof MemberDetails principal) {
            final Long memberId = principal.memberId();
            return memberRepository.findById(memberId)
                    .orElse(null);
        }
        return null;
    }
}
