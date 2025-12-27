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
                        "/admin/members/**",
                        "/admin/html/admin-main.html"
                )
                .order(0);
    }
}
