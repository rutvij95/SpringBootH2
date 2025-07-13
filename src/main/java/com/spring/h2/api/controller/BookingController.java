package com.spring.h2.api.controller;

import com.spring.h2.api.Service.PaymentService;
import com.spring.h2.api.Service.TicketService;
import com.spring.h2.api.model.*;
import com.spring.h2.api.Service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private TicketService ticketService;

    // Create a new booking
    @PostMapping
    public ResponseEntity<Booking> createBooking(@RequestBody BookingRequest request) {
        try {
            Booking booking = bookingService.createBooking(
                request.getUserId(),
                request.getShowId(),
                request.getSeatIds()
            );
            return ResponseEntity.ok(booking);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Get available seats for a show
    @GetMapping("/shows/{showId}/seats")
    public ResponseEntity<List<ShowSeat>> getAvailableSeats(@PathVariable Long showId) {
        List<ShowSeat> seats = bookingService.getAvailableSeats(showId);
        return ResponseEntity.ok(seats);
    }

    // Lock seats for booking
    @PostMapping("/shows/{showId}/seats/lock")
    public ResponseEntity<Boolean> lockSeats(
            @PathVariable Long showId,
            @RequestBody SeatLockRequest request) {
        boolean locked = bookingService.lockSeats(showId, request.getSeatIds(), request.getUserEmail());
        return ResponseEntity.ok(locked);
    }

    // Unlock seats
    @PostMapping("/shows/{showId}/seats/unlock")
    public ResponseEntity<Void> unlockSeats(
            @PathVariable Long showId,
            @RequestBody SeatUnlockRequest request) {
        bookingService.unlockSeats(showId, request.getSeatIds());
        return ResponseEntity.ok().build();
    }

    // Get booking by ID
    @GetMapping("/{bookingId}")
    public ResponseEntity<Booking> getBooking(@PathVariable String bookingId) {
        Optional<Booking> booking = bookingService.getBookingById(bookingId);
        return booking.map(ResponseEntity::ok)
                     .orElse(ResponseEntity.notFound().build());
    }

    // Get user bookings
    @GetMapping("/users/{userId}")
    public ResponseEntity<List<Booking>> getUserBookings(@PathVariable Long userId) {
        List<Booking> bookings = bookingService.getUserBookings(userId);
        return ResponseEntity.ok(bookings);
    }

    // Process payment for booking
    @PostMapping("/{bookingId}/payment")
    public ResponseEntity<Payment> processPayment(
            @PathVariable String bookingId,
            @RequestBody PaymentRequest request) {
        try {
            // Initiate payment
            Payment payment = paymentService.initiatePayment(
                bookingId,
                request.getAmount(),
                request.getPaymentMethod()
            );

            // Process payment
            payment = paymentService.processPayment(payment.getPaymentId());

            // If payment successful, confirm booking
            if (payment.getStatus() == PaymentStatus.SUCCESS) {
                bookingService.confirmBooking(bookingId, payment.getPaymentId());
            }

            return ResponseEntity.ok(payment);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Generate ticket for confirmed booking
    @PostMapping("/{bookingId}/ticket")
    public ResponseEntity<Ticket> generateTicket(@PathVariable String bookingId) {
        try {
            Ticket ticket = ticketService.generateTicket(bookingId);
            return ResponseEntity.ok(ticket);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Cancel booking
    @PostMapping("/{bookingId}/cancel")
    public ResponseEntity<Void> cancelBooking(@PathVariable String bookingId) {
        try {
            bookingService.cancelBooking(bookingId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Get shows by movie
    @GetMapping("/movies/{movieId}/shows")
    public ResponseEntity<List<Show>> getShowsByMovie(@PathVariable Long movieId) {
        List<Show> shows = bookingService.getShowsByMovie(movieId);
        return ResponseEntity.ok(shows);
    }

    // Get upcoming shows
    @GetMapping("/shows/upcoming")
    public ResponseEntity<List<Show>> getUpcomingShows() {
        List<Show> shows = bookingService.getUpcomingShows();
        return ResponseEntity.ok(shows);
    }

    // DTOs for request bodies
    public static class BookingRequest {
        private Long userId;
        private Long showId;
        private List<Long> seatIds;

        // Getters and setters
        public Long getUserId() { return userId; }
        public void setUserId(Long userId) { this.userId = userId; }
        public Long getShowId() { return showId; }
        public void setShowId(Long showId) { this.showId = showId; }
        public List<Long> getSeatIds() { return seatIds; }
        public void setSeatIds(List<Long> seatIds) { this.seatIds = seatIds; }
    }

    public static class SeatLockRequest {
        private List<Long> seatIds;
        private String userEmail;

        // Getters and setters
        public List<Long> getSeatIds() { return seatIds; }
        public void setSeatIds(List<Long> seatIds) { this.seatIds = seatIds; }
        public String getUserEmail() { return userEmail; }
        public void setUserEmail(String userEmail) { this.userEmail = userEmail; }
    }

    public static class SeatUnlockRequest {
        private List<Long> seatIds;

        // Getters and setters
        public List<Long> getSeatIds() { return seatIds; }
        public void setSeatIds(List<Long> seatIds) { this.seatIds = seatIds; }
    }

    public static class PaymentRequest {
        private java.math.BigDecimal amount;
        private String paymentMethod;

        // Getters and setters
        public java.math.BigDecimal getAmount() { return amount; }
        public void setAmount(java.math.BigDecimal amount) { this.amount = amount; }
        public String getPaymentMethod() { return paymentMethod; }
        public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
    }
}
