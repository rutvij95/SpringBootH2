package com.spring.h2.api.serviceImpl;

import com.spring.h2.api.model.Booking;
import com.spring.h2.api.Service.NotificationObserver;
import org.springframework.stereotype.Component;

@Component
public class SMSNotificationObserver implements NotificationObserver {

    @Override
    public void onBookingConfirmed(Booking booking) {
        System.out.println("SMS: Booking confirmed! ID: " + booking.getBookingId());
        System.out.println("SMS: " + booking.getShow().getMovie().getTitle() + " at " + booking.getShow().getStartTime());
        System.out.println("SMS: Sent to " + booking.getUser().getPhone());
        // In real implementation, send actual SMS
    }

    @Override
    public void onPaymentSuccess(Booking booking) {
        System.out.println("SMS: Payment of $" + booking.getTotalAmount() + " successful for booking " + booking.getBookingId());
        System.out.println("SMS: Sent to " + booking.getUser().getPhone());
        // In real implementation, send payment confirmation SMS
    }

    @Override
    public void onBookingCancelled(Booking booking) {
        System.out.println("SMS: Booking " + booking.getBookingId() + " cancelled. Refund processing.");
        System.out.println("SMS: Sent to " + booking.getUser().getPhone());
        // In real implementation, send cancellation SMS
    }

    @Override
    public void onPaymentFailed(Booking booking) {
        System.out.println("SMS: Payment failed for booking " + booking.getBookingId() + ". Please retry.");
        System.out.println("SMS: Sent to " + booking.getUser().getPhone());
        // In real implementation, send payment failure SMS
    }

    @Override
    public void onShowCancelled(Booking booking) {
        System.out.println("SMS: Show " + booking.getShow().getMovie().getTitle() + " cancelled. Full refund processing.");
        System.out.println("SMS: Sent to " + booking.getUser().getPhone());
        // In real implementation, send show cancellation SMS
    }

    @Override
    public void onTicketGenerated(Booking booking) {
        System.out.println("SMS: Ticket ready for " + booking.getShow().getMovie().getTitle() + ". Check email for QR code.");
        System.out.println("SMS: Sent to " + booking.getUser().getPhone());
        // In real implementation, send ticket generation SMS
    }
}
