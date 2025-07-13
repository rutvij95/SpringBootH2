package com.spring.h2.api.Service;

import com.spring.h2.api.model.Payment;
import com.spring.h2.api.model.PaymentStatus;
import java.math.BigDecimal;
import java.util.Optional;

public interface PaymentService {
    Payment initiatePayment(String bookingId, BigDecimal amount, String paymentMethod);
    Payment processPayment(String paymentId);
    Payment retryPayment(String paymentId);
    void refundPayment(String paymentId);
    Optional<Payment> getPaymentById(String paymentId);
    Optional<Payment> getPaymentByBookingId(String bookingId);
    PaymentStatus getPaymentStatus(String paymentId);
    boolean isPaymentSuccessful(String paymentId);
}
