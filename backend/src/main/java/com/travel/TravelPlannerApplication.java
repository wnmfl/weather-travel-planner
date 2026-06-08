package com.travel;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * 旅游计划定制系统主应用入口
 *
 * @EnableCaching: 启用缓存功能（用于缓存天气数据、景点信息等）
 * @EnableAsync: 启用异步处理（用于异步调用第三方API）
 */
@SpringBootApplication
@EnableCaching
@EnableAsync
public class TravelPlannerApplication {

    public static void main(String[] args) {
        // 启动Spring Boot应用
        SpringApplication.run(TravelPlannerApplication.class, args);

        // 启动完成后的欢迎信息
        System.out.println("\n" +
                "╔════════════════════════════════════════════════════════════╗\n" +
                "║                                                            ║\n" +
                "║      ☀️  Weather Travel Planner 启动成功！                ║\n" +
                "║      🌍 后端运行在: http://localhost:8080                ║\n" +
                "║      📱 前端运行在: http://localhost:5173                ║\n" +
                "║      📚 API文档: http://localhost:8080/api/info         ║\n" +
                "║                                                            ║\n" +
                "╚════════════════════════════════════════════════════════════╝\n");
    }
}