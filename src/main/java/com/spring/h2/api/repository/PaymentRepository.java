package com.spring.h2.api.repository;

import com.spring.h2.api.model.Payment;
import com.spring.h2.api.model.PaymentStatus;
import com.spring.h2.api.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Optional<Payment> findByPaymentId(String paymentId);
    Optional<Payment> findByBooking(Booking booking);
    Optional<Payment> findByBookingId(Long bookingId);
    List<Payment> findByStatus(PaymentStatus status);
    List<Payment> findByPaymentMethod(String paymentMethod);
    Optional<Payment> findByTransactionId(String transactionId);

    @Query("SELECT p FROM Payment p WHERE p.status = 'PENDING' AND p.createdAt < :expiryTime")
    List<Payment> findExpiredPendingPayments(@Param("expiryTime") LocalDateTime expiryTime);

    @Query("SELECT COUNT(p) FROM Payment p WHERE p.status = 'SUCCESS' AND p.processedAt >= :startTime AND p.processedAt < :endTime")
    Long countSuccessfulPaymentsByDateRange(@Param("startTime") LocalDateTime startTime,
                                           @Param("endTime") LocalDateTime endTime);
}
