package com.travel.util;

import java.util.*;

public class RouteAlgorithm {

    public static class Attraction {
        public Long id;
        public String name;
        public Double latitude;
        public Double longitude;
        public Integer visitTimeMin;
        public Integer visitTimeMax;
        public Double rating;
    }

    public static class OptimizedRoute {
        public List<Long> attractionIds;
        public Integer totalDistance;
        public Integer totalTime;
        public Double optimizationScore;
    }

    public static Integer calculateDistance(Double lat1, Double lng1, Double lat2, Double lng2) {
        double R = 6371000;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLng = Math.toRadians(lng2 - lng1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLng / 2) * Math.sin(dLng / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return (int) (R * c);
    }

    public static OptimizedRoute optimizeRoute(List<Attraction> attractions, Integer availableDays) {
        OptimizedRoute result = new OptimizedRoute();
        result.attractionIds = new ArrayList<>();
        result.totalDistance = 0;
        result.totalTime = 0;
        result.optimizationScore = 85.0;

        if (attractions != null && !attractions.isEmpty()) {
            for (Attraction a : attractions) {
                result.attractionIds.add(a.id);
                result.totalTime += a.visitTimeMin;
            }
        }

        return result;
    }
}
