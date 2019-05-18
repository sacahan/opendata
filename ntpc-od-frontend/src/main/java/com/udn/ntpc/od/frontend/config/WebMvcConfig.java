package com.udn.ntpc.od.frontend.config;

import com.udn.ntpc.od.frontend.interceptor.LoginCheckInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.http.HttpSession;
import java.util.Optional;

import static com.udn.ntpc.od.frontend.controller.PageController.USER_KEY;

@Slf4j
// Configuration的理解: https://blog.csdn.net/koflance/article/details/59304090
@Configuration
// https://docs.spring.io/spring/docs/5.1.7.RELEASE/spring-framework-reference/web.html#mvc-config-customize
public class WebMvcConfig implements WebMvcConfigurer {

    // 增加快速Mapping Controller
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        // 錯誤頁面
        registry.addViewController("/error.html").setViewName("error");
        // 預設全站根路徑轉導
        registry.addRedirectViewController("/", "/dashboard.html");
    }

    // 增加Web攔截器
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginCheckInterceptor()).addPathPatterns("/**/**.html");
    }

    // 設定CORS Header
    // CROS解決AJAX跨域問題: https://www.jianshu.com/p/e21f82495fab
    // Ref. http://tomcat.apache.org/tomcat-8.0-doc/config/filter.html#CORS_Filter
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedHeaders("Origin", "Accept", "X-Requested-With", "Content-Type", "Access-Control-Request-Method", "Access-Control-Request-Headers")
                .allowedMethods("GET", "DELETE", "POST", "HEAD", "OPTIONS")
                .allowCredentials(true);
    }

    // https://docs.spring.io/spring-data/jpa/docs/2.1.5.RELEASE/reference/html/#auditing.auditor-aware
    @Bean
    public AuditorAware auditorAware() {
        return () -> {
            // 從Session中取出登入者名稱
            ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
            HttpSession session = requestAttributes.getRequest().getSession();
            return Optional.ofNullable(session.getAttribute(USER_KEY));
        };
    }

}
