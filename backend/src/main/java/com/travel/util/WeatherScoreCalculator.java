package com.travel.util;

/**
 * 天气友好度评分计算器
 * 根据多个维度计算景点出游的舒适度评分 (0-100)
 *
 * 评分标准：
 * - 温度：15-25°C 最舒适（权重30%）
 * - 降雨：无雨最好（权重35%）
 * - 风力：0-2级最好（权重15%）
 * - 紫外线：0-5指数最好（权重10%）
 * - 湿度：40-60% 最好（权重10%）
 */
public class WeatherScoreCalculator {

    /**
     * 天气数据类
     */
    public static class WeatherData {
        public Integer temperature;      // 温度 (°C)
        public Integer humidity;         // 湿度 (%)
        public Integer rainProbability;  // 降雨概率 (%)
        public Integer windLevel;        // 风力等级 (0-12)
        public Double uvIndex;           // 紫外线指数 (0-16+)
        public String description;       // 天气描述
        public Boolean isRaining;        // 是否正在下雨
    }

    /**
     * 主方法：计算天气友好度评分
     *
     * @param weather 天气数据
     * @return 评分 0-100 分
     *
     * 举例：
     * 温度22°C, 降雨概率30%, 风力2级, 湿度60%
     * -> 温度0分, 降雨-10分, 风力0分, 湿度0分
     * -> 总分 = 100 - 10 = 90分 ✅
     */
    public static Integer calculateWeatherScore(WeatherData weather) {
        // 数据验证
        if (weather == null) {
            return 50; // 默认中等评分
        }

        int score = 100; // 初始满分

        // 逐项扣分
        score -= calculateTemperaturePenalty(weather.temperature);
        score -= calculateRainPenalty(weather.rainProbability, weather.isRaining);
        score -= calculateWindPenalty(weather.windLevel);
        score -= calculateUVPenalty(weather.uvIndex);
        score -= calculateHumidityPenalty(weather.humidity);

        // 确保分数在0-100之间
        return Math.max(0, Math.min(100, score));
    }

    /**
     * 温度惩罚评分计算
     * 舒适范围: 15-25°C
     * 可接受范围: 10-30°C
     * 不舒适范围: < 10 or > 30°C
     */
    private static Integer calculateTemperaturePenalty(Integer temperature) {
        if (temperature == null) return 0;

        // 最舒适区间：15-25°C
        if (temperature >= 15 && temperature <= 25) {
            return 0;  // 无惩罚
        }

        // 可接受区间：10-30°C
        if (temperature >= 10 && temperature <= 30) {
            // 距离舒适区间越远，惩罚越大
            // 每相差1°C扣5分
            int delta = temperature < 15 ? 15 - temperature : temperature - 25;
            return delta * 5;
        }

        // 不可接受区间
        if (temperature < 0) {
            return 50;  // 严寒，扣50分
        }
        if (temperature > 35) {
            return 50;  // 酷热，扣50分
        }
        if (temperature < 10) {
            return (10 - temperature) * 5;  // 寒冷
        }
        if (temperature > 30) {
            return (temperature - 30) * 5;  // 炎热
        }

        return 20;  // 一般不舒适
    }

    /**
     * 降雨惩罚评分计算
     * 这是最影响出游体验的因素之一
     */
    private static Integer calculateRainPenalty(
            Integer rainProbability,
            Boolean isRaining) {

        // 如果正在下雨，扣最多分
        if (isRaining != null && isRaining) {
            return 50;  // 正在下雨，扣50分
        }

        if (rainProbability == null) return 0;

        // 根据降雨概率扣分
        if (rainProbability >= 80) {
            return 50;  // 很可能下雨，很可能泡汤
        }
        if (rainProbability >= 50) {
            return 30;  // 有可能下雨，建议携带雨具
        }
        if (rainProbability >= 20) {
            return 10;  // 小概率下雨，基本不影响
        }

        return 0;  // 不太可能下雨
    }

    /**
     * 风力惩罚评分计算
     *
     * 风力等级：
     * 0-2 级: 无风/微风 (最佳出游条件)
     * 3-4 级: 和风 (仍可接受)
     * 5-6 级: 劲风 (不适宜登山等活动)
     * 7+ 级: 大风 (不建议外出，危险)
     */
    private static Integer calculateWindPenalty(Integer windLevel) {
        if (windLevel == null) return 0;

        if (windLevel <= 2) {
            return 0;  // 完美的风力条件
        }
        if (windLevel <= 4) {
            return 5;  // 轻微惩罚
        }
        if (windLevel <= 6) {
            return 20;  // 中等惩罚，不太适合户外活动
        }
        // 7 级以上：大风
        return 40;  // 严重惩罚，危险！
    }

    /**
     * 紫外线惩罚评分计算
     *
     * 紫外线指数等级：
     * 0-2: 最弱 (安全)
     * 3-5: 弱 (基本安全)
     * 6-7: 中等 (需要防晒)
     * 8-10: 强 (强烈建议防晒)
     * 11+: 极强 (必须防晒)
     */
    private static Integer calculateUVPenalty(Double uvIndex) {
        if (uvIndex == null) return 0;

        if (uvIndex <= 5) {
            return 0;  // 安全，无惩罚
        }
        if (uvIndex <= 7) {
            return 5;  // 轻微惩罚
        }
        if (uvIndex <= 10) {
            return 15;  // 中等惩罚
        }
        // 11 以上：极强紫外线
        return 25;  // 严重惩罚
    }

    /**
     * 湿度惩罚评分计算
     *
     * 湿度等级：
     * 40-60%: 最舒适 (黄金范围)
     * 30-40% 或 60-70%: 可接受 (略感不适)
     * < 30% 或 > 70%: 不舒适 (太干或太湿)
     */
    private static Integer calculateHumidityPenalty(Integer humidity) {
        if (humidity == null) return 0;

        // 最舒适：40-60%
        if (humidity >= 40 && humidity <= 60) {
            return 0;  // 完美湿度
        }

        // 可接受：30-70%
        if (humidity >= 30 && humidity <= 70) {
            return 5;  // 轻微不适
        }

        // 不舒适
        if (humidity < 30) {
            return 10;  // 太干，口干舌燥
        }
        if (humidity > 80) {
            return 15;  // 太湿，闷热难受
        }

        return 10;
    }

    /**
     * 获取天气评分的文字描述和星级评价
     *
     * @param score 评分（0-100）
     * @return 文字描述
     *
     * 示例：
     * 95分 -> "⭐⭐⭐⭐⭐ 极好 - 完美出游天气"
     * 50分 -> "⭐⭐ 一般 - 建议调整景点"
     */
    public static String getWeatherGrade(Integer score) {
        if (score == null) score = 50;

        if (score >= 90) {
            return "⭐⭐⭐⭐⭐ 极好 - 完美出游天气！";
        }
        if (score >= 75) {
            return "⭐⭐⭐⭐ 很好 - 非常适合出游";
        }
        if (score >= 60) {
            return "⭐⭐⭐ 良好 - 可以出游";
        }
        if (score >= 45) {
            return "⭐⭐ 一般 - 建议调整景点";
        }
        if (score >= 30) {
            return "⭐ 较差 - 不太适合出游";
        }
        return "❌ 很差 - 不建议出游！";
    }

    /**
     * 获取基于天气的出游建议
     * 给用户具体的行动建议
     *
     * @param weather 天气数据
     * @param score 天气评分
     * @return 建议文字
     */
    public static String getSuggestion(WeatherData weather, Integer score) {
        if (weather == null || score == null) return "";

        StringBuilder suggestion = new StringBuilder();

        // 温度建议
        if (weather.temperature != null) {
            if (weather.temperature < 10) {
                suggestion.append("🧥 温度较低，建议穿厚衣服。");
            } else if (weather.temperature > 30) {
                suggestion.append("☀️ 温度较高，建议防晒并补充水分。");
            }
        }

        // 降雨建议
        if (weather.rainProbability != null && weather.rainProbability > 60) {
            suggestion.append("☔ 降雨概率较高，建议携带雨具。");
        }

        // 风力建议
        if (weather.windLevel != null && weather.windLevel > 5) {
            suggestion.append("💨 风力较强，登山等活动需谨慎。");
        }

        // 紫外线建议
        if (weather.uvIndex != null && weather.uvIndex > 8) {
            suggestion.append("🕶️ 紫外线强，建议做好防晒。");
        }

        return suggestion.toString();
    }
}