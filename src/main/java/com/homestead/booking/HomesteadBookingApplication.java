package com.homestead.booking;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 民宿预订平台启动类
 *
 * @author homestead
 * @since 2025-11-18
 */
@SpringBootApplication
@MapperScan("com.homestead.booking.mapper")
public class HomesteadBookingApplication {

    public static void main(String[] args) {
        SpringApplication.run(HomesteadBookingApplication.class, args);
        System.out.println("\n========================================");
        System.out.println("民宿预订平台启动成功!");
        System.out.println("接口文档地址: http://localhost:8080/api/doc.html");
        System.out.println("========================================\n");
    }

}
