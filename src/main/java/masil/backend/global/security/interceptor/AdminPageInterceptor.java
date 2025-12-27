package masil.backend.global.security.interceptor;

import static masil.backend.global.util.SessionConst.ADMIN_SESSION_KEY;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import masil.backend.modules.adminAuth.entity.AdminMember;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;


@Slf4j
@Component
public class AdminPageInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestURI = request.getRequestURI();

        HttpSession session = request.getSession(false);
        if (session == null) {
            log.warn("인증되지 않은 접근 시도: uri={}", requestURI);
            redirectToLogin(response);
            return false;
        }

        AdminMember adminMember = (AdminMember) session.getAttribute(ADMIN_SESSION_KEY);
        if (adminMember == null) {
            log.warn("세션은 존재하나 관리자 정보 없음: uri={}", requestURI);
            redirectToLogin(response);
            return false;
        }

        log.debug("관리자 접근 허용: adminId={}, uri={}", adminMember.getAdminId(), requestURI);
        return true;
    }

    private void redirectToLogin(HttpServletResponse response) throws Exception {
        response.sendRedirect("/admin/login");
    }
}
