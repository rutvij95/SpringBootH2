package com.spring.h2.api.serviceImpl;

import com.spring.h2.api.model.Booking;
import com.spring.h2.api.Service.NotificationObserver;
import org.springframework.stereotype.Component;

@Component
public class EmailNotificationObserver implements NotificationObserver {

    @Override
    public void onBookingConfirmed(Booking booking) {
        System.out.println("EMAIL: Booking confirmed for " + booking.getUser().getEmail());
        System.out.println("EMAIL: Booking ID: " + booking.getBookingId());
        System.out.println("EMAIL: Movie: " + booking.getShow().getMovie().getTitle());
        System.out.println("EMAIL: Show Time: " + booking.getShow().getStartTime());
        // In real implementation, send actual email
    }

    @Override
    public void onPaymentSuccess(Booking booking) {
        System.out.println("EMAIL: Payment successful for booking " + booking.getBookingId());
        System.out.println("EMAIL: Amount paid: $" + booking.getTotalAmount());
        // In real implementation, send payment receipt email
    }

    @Override
    public void onBookingCancelled(Booking booking) {
        System.out.println("EMAIL: Booking cancelled for " + booking.getUser().getEmail());
        System.out.println("EMAIL: Booking ID: " + booking.getBookingId());
        System.out.println("EMAIL: Refund will be processed within 3-5 business days");
        // In real implementation, send cancellation email
    }

    @Override
    public void onPaymentFailed(Booking booking) {
        System.out.println("EMAIL: Payment failed for booking " + booking.getBookingId());
        System.out.println("EMAIL: Please try again with a different payment method");
        // In real implementation, send payment failure email
    }

    @Override
    public void onShowCancelled(Booking booking) {
        System.out.println("EMAIL: Show cancelled - " + booking.getShow().getMovie().getTitle());
        System.out.println("EMAIL: Full refund will be processed automatically");
        // In real implementation, send show cancellation email
    }

    @Override
    public void onTicketGenerated(Booking booking) {
        System.out.println("EMAIL: Ticket generated for booking " + booking.getBookingId());
        System.out.println("EMAIL: Please save this ticket for entry");
        // In real implementation, send ticket email with QR code
    }
}
