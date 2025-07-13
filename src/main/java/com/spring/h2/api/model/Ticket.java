package com.spring.h2.api.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "tickets")
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String ticketId;

    @Column(nullable = false)
    private String qrCode;

    @Column(nullable = false)
    private LocalDateTime generatedAt;

    @Column
    private LocalDateTime scannedAt;

    @Column(nullable = false)
    private Boolean isUsed;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_id", nullable = false)
    private Booking booking;

    public Ticket() {
        this.ticketId = UUID.randomUUID().toString();
        this.generatedAt = LocalDateTime.now();
        this.isUsed = false;
    }

    public Ticket(Booking booking, String qrCode) {
        this();
        this.booking = booking;
        this.qrCode = qrCode;
    }

    // Utility methods
    public void markAsUsed() {
        this.isUsed = true;
        this.scannedAt = LocalDateTime.now();
    }

    public boolean isValid() {
        return !isUsed && booking.getShow().getStartTime().isAfter(LocalDateTime.now());
    }

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTicketId() { return ticketId; }
    public void setTicketId(String ticketId) { this.ticketId = ticketId; }

    public String getQrCode() { return qrCode; }
    public void setQrCode(String qrCode) { this.qrCode = qrCode; }

    public LocalDateTime getGeneratedAt() { return generatedAt; }
    public void setGeneratedAt(LocalDateTime generatedAt) { this.generatedAt = generatedAt; }

    public LocalDateTime getScannedAt() { return scannedAt; }
    public void setScannedAt(LocalDateTime scannedAt) { this.scannedAt = scannedAt; }

    public Boolean getIsUsed() { return isUsed; }
    public void setIsUsed(Boolean isUsed) { this.isUsed = isUsed; }

    public Booking getBooking() { return booking; }
    public void setBooking(Booking booking) { this.booking = booking; }
}
