package com.homestead.booking.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Knife4j接口文档配置
 *
 * @author homestead
 * @since 2025-12-03
 */
@Configuration
public class Knife4jConfig {

    /**
     * 全局API配置
     */
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("民宿管理与推荐平台API文档")
                        .version("1.0.0")
                        .description("基于鸿蒙的民宿管理与推荐平台后端接口文档")
                        .contact(new Contact()
                                .name("Homestead Team")
                                .email("homestead@example.com"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")));
    }

//    /**
//     * 用户管理模块API
//     */
//    @Bean
//    public GroupedOpenApi userApi() {
//        return GroupedOpenApi.builder()
//                .group("用户管理")
//                .pathsToMatch("/user/**")
//                .build();
//    }
//
//    /**
//     * 房源管理模块API
//     */
//    @Bean
//    public GroupedOpenApi homesteadApi() {
//        return GroupedOpenApi.builder()
//                .group("房源管理")
//                .pathsToMatch("/homestead/**")
//                .build();
//    }
//
//    /**
//     * 订单管理模块API
//     */
//    @Bean
//    public GroupedOpenApi orderApi() {
//        return GroupedOpenApi.builder()
//                .group("订单管理")
//                .pathsToMatch("/order/**")
//                .build();
//    }
//
//    /**
//     * 评价管理模块API
//     */
//    @Bean
//    public GroupedOpenApi reviewApi() {
//        return GroupedOpenApi.builder()
//                .group("评价管理")
//                .pathsToMatch("/review/**")
//                .build();
//    }
//
//    /**
//     * 收藏管理模块API
//     */
//    @Bean
//    public GroupedOpenApi favoriteApi() {
//        return GroupedOpenApi.builder()
//                .group("收藏管理")
//                .pathsToMatch("/favorite/**")
//                .build();
//    }

}
