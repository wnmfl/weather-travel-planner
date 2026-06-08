package com.travel.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 首页和系统信息控制器
 *
 * 提供基础的健康检查和项目信息接口
 */
@RestController
@RequestMapping("/api")
public class HomeController {

    /**
     * 健康检查接口
     * 用于监控系统是否正常运行
     *
     * GET /api/health
     *
     * @return 系统状态信息
     *
     * 示例响应：
     * {
     *   "status": "UP",
     *   "message": "Weather Travel Planner Backend is running",
     *   "timestamp": 1694000000000
     * }
     */
    @GetMapping("/health")
    public Map<String, Object> health() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "UP");                                    // 状态：UP=正常, DOWN=异常
        response.put("message", "Weather Travel Planner Backend is running");
        response.put("timestamp", System.currentTimeMillis());           // 时间戳
        return response;
    }

    /**
     * 获取项目信息接口
     * 返回系统的基本信息和功能列表
     *
     * GET /api/info
     *
     * @return 项目信息
     *
     * 示例响应：
     * {
     *   "name": "Weather Travel Planner",
     *   "version": "0.1.0",
     *   "description": "A weather-aware travel planning system",
     *   "features": ["景点搜索", "天气查询", "路线规划", "门票预订", "用户评论"]
     * }
     */
    @GetMapping("/info")
    public Map<String, Object> info() {
        Map<String, Object> response = new HashMap<>();

        response.put("name", "Weather Travel Planner");
        response.put("version", "0.1.0");
        response.put("description", "A weather-aware travel planning system with intelligent route planning");

        // 系统支持的功能列表
        response.put("features", new String[]{
                "🔍 景点搜索 - 按城市、分类、价格搜索景点",
                "🌤️ 天气查询 - 获取7天天气预报",
                "🗺️ 路线规划 - 智能规划最优旅游路线",
                "💰 门票预订 - 在线预订景点门票",
                "⭐ 用户评论 - 查看和发布景点评论"
        });

        // 技术栈信息
        Map<String, String> techStack = new HashMap<>();
        techStack.put("backend", "Spring Boot 2.7.18");
        techStack.put("language", "Java 17");
        techStack.put("database", "MySQL 8.0");
        techStack.put("cache", "Redis");
        response.put("techStack", techStack);

        return response;
    }

    /**
     * 项目首页
     * GET /api/
     *
     * @return 欢迎信息
     */
    @GetMapping("/")
    public Map<String, String> welcome() {
        Map<String, String> response = new HashMap<>();
        response.put("message", "Welcome to Weather Travel Planner API");
        response.put("usage", "访问 /api/health 检查系统状态，访问 /api/info 获取项目信息");
        response.put("docs", "详细API文档请参考 /docs/API.md");
        return response;
    }
}