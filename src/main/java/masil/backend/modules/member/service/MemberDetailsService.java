package masil.backend.modules.member.service;


import lombok.RequiredArgsConstructor;
import masil.backend.global.security.dto.MemberDetails;
import masil.backend.modules.member.entity.Member;
import masil.backend.modules.member.repository.MemberRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(final String userId) {
        final Long memberId = Long.parseLong(userId);
        final Member member = memberRepository.findById(memberId)
                .filter(m -> !m.getIsDeleted())
                .orElseThrow(() -> new MemberException(NOT_FOUND_MEMBER));

        return new MemberDetails(
                member.getId(),
                member.getName(),
                member.getPassword(),
                member.getRoles().stream()
                        .map(MemberRoleType::name)
                        .toList()
        );
    }
}
