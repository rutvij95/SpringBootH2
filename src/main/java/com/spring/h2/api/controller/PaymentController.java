package com.spring.h2.api.controller;

import com.spring.h2.api.model.Payment;
import com.spring.h2.api.model.PaymentStatus;
import com.spring.h2.api.Service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Optional;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    // Get payment by ID
    @GetMapping("/{paymentId}")
    public ResponseEntity<Payment> getPayment(@PathVariable String paymentId) {
        Optional<Payment> payment = paymentService.getPaymentById(paymentId);
        return payment.map(ResponseEntity::ok)
                     .orElse(ResponseEntity.notFound().build());
    }

    // Get payment by booking ID
    @GetMapping("/bookings/{bookingId}")
    public ResponseEntity<Payment> getPaymentByBooking(@PathVariable String bookingId) {
        Optional<Payment> payment = paymentService.getPaymentByBookingId(bookingId);
        return payment.map(ResponseEntity::ok)
                     .orElse(ResponseEntity.notFound().build());
    }

    // Get payment status
    @GetMapping("/{paymentId}/status")
    public ResponseEntity<PaymentStatus> getPaymentStatus(@PathVariable String paymentId) {
        try {
            PaymentStatus status = paymentService.getPaymentStatus(paymentId);
            return ResponseEntity.ok(status);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Initiate payment
    @PostMapping
    public ResponseEntity<Payment> initiatePayment(@RequestBody PaymentInitRequest request) {
        try {
            Payment payment = paymentService.initiatePayment(
                request.getBookingId(),
                request.getAmount(),
                request.getPaymentMethod()
            );
            return ResponseEntity.ok(payment);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Process payment
    @PostMapping("/{paymentId}/process")
    public ResponseEntity<Payment> processPayment(@PathVariable String paymentId) {
        try {
            Payment payment = paymentService.processPayment(paymentId);
            return ResponseEntity.ok(payment);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Retry payment
    @PostMapping("/{paymentId}/retry")
    public ResponseEntity<Payment> retryPayment(@PathVariable String paymentId) {
        try {
            Payment payment = paymentService.retryPayment(paymentId);
            return ResponseEntity.ok(payment);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Refund payment
    @PostMapping("/{paymentId}/refund")
    public ResponseEntity<Void> refundPayment(@PathVariable String paymentId) {
        try {
            paymentService.refundPayment(paymentId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Check if payment is successful
    @GetMapping("/{paymentId}/success")
    public ResponseEntity<Boolean> isPaymentSuccessful(@PathVariable String paymentId) {
        boolean isSuccessful = paymentService.isPaymentSuccessful(paymentId);
        return ResponseEntity.ok(isSuccessful);
    }

    // DTO for payment initiation
    public static class PaymentInitRequest {
        private String bookingId;
        private BigDecimal amount;
        private String paymentMethod;

        // Getters and setters
        public String getBookingId() { return bookingId; }
        public void setBookingId(String bookingId) { this.bookingId = bookingId; }
        public BigDecimal getAmount() { return amount; }
        public void setAmount(BigDecimal amount) { this.amount = amount; }
        public String getPaymentMethod() { return paymentMethod; }
        public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
    }
}
