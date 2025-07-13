package com.spring.h2.api.Service;

import com.spring.h2.api.model.Booking;

public interface NotificationObserver {
    void onBookingConfirmed(Booking booking);
    void onPaymentSuccess(Booking booking);
    void onBookingCancelled(Booking booking);
    void onPaymentFailed(Booking booking);
    void onShowCancelled(Booking booking);
    void onTicketGenerated(Booking booking);
}
