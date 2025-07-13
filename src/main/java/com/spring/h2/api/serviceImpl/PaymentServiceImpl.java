package com.spring.h2.api.serviceImpl;

import com.spring.h2.api.model.Payment;
import com.spring.h2.api.model.PaymentStatus;
import com.spring.h2.api.model.Booking;
import com.spring.h2.api.repository.PaymentRepository;
import com.spring.h2.api.repository.BookingRepository;
import com.spring.h2.api.Service.PaymentService;
import com.spring.h2.api.Service.NotificationService;
import com.spring.h2.api.Service.payment.PaymentGateway;
import com.spring.h2.api.Service.payment.PaymentGatewayFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private PaymentGatewayFactory paymentGatewayFactory;

    @Autowired
    private NotificationService notificationService;

    @Override
    @Transactional
    public Payment initiatePayment(String bookingId, BigDecimal amount, String paymentMethod) {
        // Find booking
        Booking booking = bookingRepository.findByBookingId(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        // Validate payment method
        if (!paymentGatewayFactory.isPaymentMethodSupported(paymentMethod)) {
            throw new RuntimeException("Unsupported payment method: " + paymentMethod);
        }

        // Create payment record
        Payment payment = new Payment(booking, amount, paymentMethod);
        payment = paymentRepository.save(payment);

        return payment;
    }

    @Override
    @Transactional
    public Payment processPayment(String paymentId) {
        Payment payment = paymentRepository.findByPaymentId(paymentId)
                .orElseThrow(() -> new RuntimeException("Payment not found"));

        if (payment.getStatus() != PaymentStatus.PENDING) {
            throw new RuntimeException("Payment is not in pending state");
        }

        try {
            // Mark as processing
            payment.markAsProcessing();
            paymentRepository.save(payment);

            // Get appropriate payment gateway using Factory pattern
            PaymentGateway gateway = paymentGatewayFactory.getPaymentGateway(payment.getPaymentMethod());

            // Process payment using Strategy pattern
            String transactionId = gateway.processPayment(
                payment.getPaymentId(),
                payment.getAmount(),
                payment.getPaymentMethod()
            );

            // Mark as successful
            payment.markAsSuccess(transactionId);
            payment = paymentRepository.save(payment);

            // Notify observers
            notificationService.notifyObservers("PAYMENT_SUCCESS", payment.getBooking());

            return payment;

        } catch (Exception e) {
            // Mark as failed
            payment.markAsFailed(e.getMessage());
            payment = paymentRepository.save(payment);

            // Notify observers
            notificationService.notifyObservers("PAYMENT_FAILED", payment.getBooking());

            throw new RuntimeException("Payment processing failed: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional
    public Payment retryPayment(String paymentId) {
        Payment payment = paymentRepository.findByPaymentId(paymentId)
                .orElseThrow(() -> new RuntimeException("Payment not found"));

        if (payment.getStatus() != PaymentStatus.FAILED) {
            throw new RuntimeException("Payment retry is only allowed for failed payments");
        }

        // Reset payment status to pending
        payment.setStatus(PaymentStatus.PENDING);
        payment.setGatewayResponse(null);
        payment = paymentRepository.save(payment);

        // Process payment again
        return processPayment(paymentId);
    }

    @Override
    @Transactional
    public void refundPayment(String paymentId) {
        Payment payment = paymentRepository.findByPaymentId(paymentId)
                .orElseThrow(() -> new RuntimeException("Payment not found"));

        if (payment.getStatus() != PaymentStatus.SUCCESS) {
            throw new RuntimeException("Can only refund successful payments");
        }

        try {
            // Get appropriate payment gateway
            PaymentGateway gateway = paymentGatewayFactory.getPaymentGateway(payment.getPaymentMethod());

            // Process refund
            gateway.refundPayment(payment.getTransactionId(), payment.getAmount());

            // Mark as refunded
            payment.markAsRefunded();
            paymentRepository.save(payment);

        } catch (Exception e) {
            throw new RuntimeException("Refund processing failed: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<Payment> getPaymentById(String paymentId) {
        return paymentRepository.findByPaymentId(paymentId);
    }

    @Override
    public Optional<Payment> getPaymentByBookingId(String bookingId) {
        Booking booking = bookingRepository.findByBookingId(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));
        return paymentRepository.findByBooking(booking);
    }

    @Override
    public PaymentStatus getPaymentStatus(String paymentId) {
        Payment payment = paymentRepository.findByPaymentId(paymentId)
                .orElseThrow(() -> new RuntimeException("Payment not found"));
        return payment.getStatus();
    }

    @Override
    public boolean isPaymentSuccessful(String paymentId) {
        Optional<Payment> payment = paymentRepository.findByPaymentId(paymentId);
        return payment.isPresent() && payment.get().getStatus() == PaymentStatus.SUCCESS;
    }
}
