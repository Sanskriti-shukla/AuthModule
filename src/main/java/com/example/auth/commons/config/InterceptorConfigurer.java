package com.example.auth.commons.config;
import com.example.auth.commons.intercepter.Interceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@Slf4j
public class InterceptorConfigurer implements WebMvcConfigurer {
    private final Interceptor interceptor;

    public InterceptorConfigurer(Interceptor interceptor) {
        this.interceptor = interceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(interceptor).excludePathPatterns(
                "/swagger-ui.html",
                "/webjars/**",
                "/swagger-resources",
                "/swagger-resources/**",
                "/v2/api-docs",
                "/configuration/security",
                "/configuration/ui",
                "/error"
        );
        log.info("Adding Login authentication interceptor");
        WebMvcConfigurer.super.addInterceptors(registry);
    }

    @Override
    public void addCorsMappings (CorsRegistry registry){
        registry.addMapping("/**");
    }
    @Override
    public void addResourceHandlers (ResourceHandlerRegistry registry){
        registry
                .addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        registry
                .addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }
}


