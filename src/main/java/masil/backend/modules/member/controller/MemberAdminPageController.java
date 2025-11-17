package masil.backend.modules.member.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class MemberAdminPageController {

    @GetMapping
    public String adminPage() {
        return "redirect:/admin/html/admin-main.html";
    }
}
