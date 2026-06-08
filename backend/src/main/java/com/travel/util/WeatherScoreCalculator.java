package com.travel.util;

public class WeatherScoreCalculator {

    public static class WeatherData {
        public Integer temperature;
        public Integer humidity;
        public Integer rainProbability;
        public Integer windLevel;
        public Double uvIndex;
        public String description;
        public Boolean isRaining;
    }

    public static Integer calculateWeatherScore(WeatherData weather) {
        if (weather == null) {
            return 50;
        }

        int score = 100;

        if (weather.temperature != null) {
            if (weather.temperature < 5 || weather.temperature > 35) {
                score -= 40;
            } else if (weather.temperature < 15 || weather.temperature > 25) {
                score -= 20;
            }
        }

        if (weather.rainProbability != null) {
            if (weather.rainProbability > 80) {
                score -= 50;
            } else if (weather.rainProbability > 50) {
                score -= 25;
            }
        }

        if (weather.windLevel != null && weather.windLevel > 5) {
            score -= 30;
        }

        if (weather.uvIndex != null && weather.uvIndex > 8) {
            score -= 15;
        }

        return Math.max(0, Math.min(100, score));
    }

    public static String getWeatherGrade(Integer score) {
        if (score == null) score = 50;
        if (score >= 90) return "极好";
        if (score >= 75) return "很好";
        if (score >= 60) return "良好";
        if (score >= 45) return "一般";
        if (score >= 30) return "较差";
        return "很差";
    }
}
