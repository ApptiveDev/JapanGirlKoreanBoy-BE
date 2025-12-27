package masil.backend.global.config;

import lombok.RequiredArgsConstructor;
import masil.backend.global.security.interceptor.AdminPageInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class AdminWebConfig implements WebMvcConfigurer {

    private final AdminPageInterceptor adminPageInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(adminPageInterceptor)
                .addPathPatterns(
                        "/admin/html/admin-main.html",  // 관리자 메인 페이지만
                        "/admin/members/**"              // 관리자 API
                )
                .order(0);
    }
}
