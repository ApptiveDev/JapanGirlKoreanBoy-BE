package masil.backend.modules.adminMember.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import masil.backend.modules.adminAuth.entity.AdminMember;
import masil.backend.modules.adminMember.dto.request.AdminMemberLoginRequest;
import masil.backend.modules.adminMember.repository.AdminMemberRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminMemberAuthService {

    private final AdminMemberRepository adminMemberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public AdminMember authenticateAdmin(AdminMemberLoginRequest request) {
        AdminMember adminMember = adminMemberRepository
                .findByAdminId(request.adminId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 관리자 ID입니다."));

        if (!bCryptPasswordEncoder.matches(request.password(), adminMember.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        log.info("관리자 로그인 성공: adminId={}", request.adminId());
        return adminMember;
    }
}
