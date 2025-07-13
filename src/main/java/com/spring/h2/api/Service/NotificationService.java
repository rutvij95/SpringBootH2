package com.spring.h2.api.Service;

import com.spring.h2.api.model.Booking;
import com.spring.h2.api.model.User;

public interface NotificationService {
    void sendBookingConfirmation(Booking booking);
    void sendPaymentConfirmation(Booking booking);
    void sendTicketGenerated(Booking booking);
    void sendBookingCancellation(Booking booking);
    void sendPaymentFailure(Booking booking);
    void sendShowCancellation(Booking booking);
    void sendReminder(Booking booking);

    // Observer pattern methods
    void subscribe(NotificationObserver observer);
    void unsubscribe(NotificationObserver observer);
    void notifyObservers(String eventType, Booking booking);
}
