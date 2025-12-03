//package com.homestead.booking.config;
//
//import com.homestead.booking.interceptor.JwtInterceptor;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.config.annotation.CorsRegistry;
//import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
///**
// * Web MVC配置类
// *
// * @author homestead
// * @since 2025-11-18
// */
//@Configuration
//public class WebMvcConfig implements WebMvcConfigurer {
//
//    @Bean
//    public JwtInterceptor jwtInterceptor() {
//        return new JwtInterceptor();
//    }
//
//    /**
//     * 配置跨域
//     */
//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//        registry.addMapping("/**")
//                .allowedOriginPatterns("*")
//                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
//                .allowedHeaders("*")
//                .allowCredentials(true)
//                .maxAge(3600);
//    }
//
//    /**
//     * 配置拦截器
//     */
//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(jwtInterceptor())
//                .addPathPatterns("/**")
//                .excludePathPatterns(
//                        "/user/register",
//                        "/user/login",
//                        "/user/sendVerifyCode",
//                        "/homestead/list",
//                        "/homestead/detail/**",
//                        "/homestead/search"
//                );
//    }
//
//}
