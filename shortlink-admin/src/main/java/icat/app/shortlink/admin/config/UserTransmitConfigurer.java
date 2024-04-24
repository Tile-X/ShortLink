package icat.app.shortlink.admin.config;

import icat.app.shortlink.admin.intercept.UserTransmitIntercept;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Nonnull;
/**
 * 用户上下文处理拦截器配置类
 */
@Configuration
@RequiredArgsConstructor
public class UserTransmitConfigurer implements WebMvcConfigurer {

    private final UserTransmitIntercept userTransmitIntercept;

    @Override
    public void addInterceptors(@Nonnull InterceptorRegistry registry) {
        WebMvcConfigurer.super.addInterceptors(registry);
        registry.addInterceptor(userTransmitIntercept)
                .addPathPatterns("/api/short-link/admin/v1/user/**")
                .addPathPatterns("/api/short-link/admin/v1/group/**")
                .excludePathPatterns("/api/short-link/admin/v1/user/login")
                .excludePathPatterns("/api/short-link/admin/v1/user/register")
                .excludePathPatterns("/api/short-link/admin/v1/user/check-login")
                .excludePathPatterns("/api/short-link/admin/v1/user/username-available");
    }

}
