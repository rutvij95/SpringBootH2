package com.spring.h2.api.serviceImpl;

import com.spring.h2.api.model.Booking;
import com.spring.h2.api.Service.NotificationService;
import com.spring.h2.api.Service.NotificationObserver;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class NotificationServiceImpl implements NotificationService {

    private final List<NotificationObserver> observers = new CopyOnWriteArrayList<>();

    @Override
    public void sendBookingConfirmation(Booking booking) {
        System.out.println("Sending booking confirmation to: " + booking.getUser().getEmail());
        System.out.println("Booking ID: " + booking.getBookingId());
        System.out.println("Show: " + booking.getShow().getMovie().getTitle());
        System.out.println("Theater: " + booking.getShow().getScreen().getTheater().getName());
        System.out.println("Show Time: " + booking.getShow().getStartTime());
        System.out.println("Total Amount: $" + booking.getTotalAmount());
    }

    @Override
    public void sendPaymentConfirmation(Booking booking) {
        System.out.println("Sending payment confirmation to: " + booking.getUser().getEmail());
        System.out.println("Payment successful for booking: " + booking.getBookingId());
        System.out.println("Amount: $" + booking.getTotalAmount());
    }

    @Override
    public void sendTicketGenerated(Booking booking) {
        System.out.println("Sending ticket to: " + booking.getUser().getEmail());
        System.out.println("Ticket generated for booking: " + booking.getBookingId());
        System.out.println("Please save this ticket for entry to the theater");
    }

    @Override
    public void sendBookingCancellation(Booking booking) {
        System.out.println("Sending cancellation notification to: " + booking.getUser().getEmail());
        System.out.println("Booking " + booking.getBookingId() + " has been cancelled");
        System.out.println("Refund will be processed within 3-5 business days");
    }

    @Override
    public void sendPaymentFailure(Booking booking) {
        System.out.println("Sending payment failure notification to: " + booking.getUser().getEmail());
        System.out.println("Payment failed for booking: " + booking.getBookingId());
        System.out.println("Please try again with a different payment method");
    }

    @Override
    public void sendShowCancellation(Booking booking) {
        System.out.println("Sending show cancellation notification to: " + booking.getUser().getEmail());
        System.out.println("Show " + booking.getShow().getMovie().getTitle() + " has been cancelled");
        System.out.println("Full refund will be processed automatically");
    }

    @Override
    public void sendReminder(Booking booking) {
        System.out.println("Sending reminder to: " + booking.getUser().getEmail());
        System.out.println("Reminder: Your show starts at " + booking.getShow().getStartTime());
        System.out.println("Please arrive 30 minutes early");
    }

    // Observer pattern implementation
    @Override
    public void subscribe(NotificationObserver observer) {
        observers.add(observer);
    }

    @Override
    public void unsubscribe(NotificationObserver observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers(String eventType, Booking booking) {
        for (NotificationObserver observer : observers) {
            switch (eventType) {
                case "BOOKING_CREATED":
                case "BOOKING_CONFIRMED":
                    sendBookingConfirmation(booking);
                    observer.onBookingConfirmed(booking);
                    break;
                case "PAYMENT_SUCCESS":
                    sendPaymentConfirmation(booking);
                    observer.onPaymentSuccess(booking);
                    break;
                case "BOOKING_CANCELLED":
                    sendBookingCancellation(booking);
                    observer.onBookingCancelled(booking);
                    break;
                case "PAYMENT_FAILED":
                    sendPaymentFailure(booking);
                    observer.onPaymentFailed(booking);
                    break;
                case "SHOW_CANCELLED":
                    sendShowCancellation(booking);
                    observer.onShowCancelled(booking);
                    break;
                case "TICKET_GENERATED":
                    sendTicketGenerated(booking);
                    observer.onTicketGenerated(booking);
                    break;
                default:
                    System.out.println("Unknown event type: " + eventType);
            }
        }
    }
}
