package masil.backend.modules.adminMember.controller;

import static masil.backend.global.util.SessionConst.ADMIN_SESSION_KEY;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import masil.backend.modules.adminAuth.entity.AdminMember;
import masil.backend.modules.adminMember.dto.request.AdminMemberLoginRequest;
import masil.backend.modules.adminMember.service.AdminMemberAuthService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@Slf4j
@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminMemberPageController {

    private final AdminMemberAuthService adminAuthService;

    @Value("${admin.session.timeout:7200}")
    private int sessionTimeout;

    @GetMapping("/html/admin-main.html")
    public String adminPage(HttpSession session) {
        // 세션 체크는 Interceptor에서 처리됨
        return "forward:/admin/html/admin-main.html";
    }

    @GetMapping
    public String adminHome(HttpSession session) {
        // 이미 로그인되어 있으면 관리자 페이지로
        if (session.getAttribute(ADMIN_SESSION_KEY) != null) {
            return "redirect:/admin/html/admin-main.html";
        }
        return "redirect:/admin/html/admin-login.html";
    }

    @GetMapping("/login")
    public String loginPage(HttpSession session) {
        // 이미 로그인되어 있으면 관리자 페이지로
        if (session.getAttribute(ADMIN_SESSION_KEY) != null) {
            return "redirect:/admin/html/admin-main.html";
        }
        return "redirect:/admin/html/admin-login.html";
    }

    @PostMapping("/login")
    @ResponseBody
    public String login(@RequestParam String adminId,
                        @RequestParam String password,
                        HttpSession session) {

        try {
            AdminMemberLoginRequest request = new AdminMemberLoginRequest(adminId, password);
            AdminMember adminMember = adminAuthService.authenticateAdmin(request);

            session.setAttribute(ADMIN_SESSION_KEY, adminMember);
            session.setMaxInactiveInterval(sessionTimeout);

            log.info("관리자 로그인 성공 및 세션 생성: adminId={}, sessionId={}",
                    adminMember.getAdminId(), session.getId());

            // 성공 시 리다이렉트 응답 (fetch API에서 처리)
            return "SUCCESS";

        } catch (IllegalArgumentException e) {
            log.warn("로그인 실패: {}", e.getMessage());
            return "ERROR:" + e.getMessage();
        } catch (Exception e) {
            log.error("로그인 처리 중 예외 발생", e);
            return "ERROR:로그인 처리 중 오류가 발생했습니다.";
        }
    }

    @PostMapping("/logout")
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            log.info("관리자 로그아웃: sessionId={}", session.getId());
            session.invalidate();
        }
        return "redirect:/admin/html/admin-login.html";
    }
}
