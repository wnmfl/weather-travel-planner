package com.travel;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TravelPlannerApplication {
    public static void main(String[] args) {
        SpringApplication.run(TravelPlannerApplication.class, args);
        System.out.println("\nWeather Travel Planner Backend started at http://localhost:8080\n");
    }
}
