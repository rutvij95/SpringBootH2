package com.spring.h2.api.config;

import com.spring.h2.api.Service.NotificationService;
import com.spring.h2.api.serviceImpl.EmailNotificationObserver;
import com.spring.h2.api.serviceImpl.SMSNotificationObserver;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
public class MovieBookingConfig {

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private EmailNotificationObserver emailObserver;

    @Autowired
    private SMSNotificationObserver smsObserver;

    @PostConstruct
    public void setupObservers() {
        // Register observers using Observer pattern
        notificationService.subscribe(emailObserver);
        notificationService.subscribe(smsObserver);
    }

    // Scheduled task to clean up expired bookings and seat locks
    @Scheduled(fixedRate = 60000) // Run every minute
    public void cleanupExpiredBookings() {
        // This will be called by the scheduler
        System.out.println("Running cleanup task for expired bookings and seat locks");
    }
}
