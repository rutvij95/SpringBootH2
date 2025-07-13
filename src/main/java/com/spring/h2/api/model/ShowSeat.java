package com.spring.h2.api.model;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "show_seats")
public class ShowSeat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SeatStatus status;

    @Column
    private LocalDateTime lockedUntil;

    @Column
    private String lockedBy; // User email or session ID

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "show_id", nullable = false)
    private Show show;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seat_id", nullable = false)
    private Seat seat;

    @OneToOne(mappedBy = "showSeat", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private BookingSeat bookingSeat;

    public ShowSeat() {
        this.status = SeatStatus.AVAILABLE;
    }

    public ShowSeat(BigDecimal price, Show show, Seat seat) {
        this();
        this.price = price;
        this.show = show;
        this.seat = seat;
    }

    // Utility methods
    public boolean isAvailable() {
        return status == SeatStatus.AVAILABLE;
    }

    public boolean isLocked() {
        return status == SeatStatus.LOCKED && lockedUntil != null && lockedUntil.isAfter(LocalDateTime.now());
    }

    public boolean isBooked() {
        return status == SeatStatus.BOOKED;
    }

    public void lockSeat(String userEmail, int lockDurationMinutes) {
        this.status = SeatStatus.LOCKED;
        this.lockedBy = userEmail;
        this.lockedUntil = LocalDateTime.now().plusMinutes(lockDurationMinutes);
    }

    public void unlockSeat() {
        this.status = SeatStatus.AVAILABLE;
        this.lockedBy = null;
        this.lockedUntil = null;
    }

    public void bookSeat() {
        this.status = SeatStatus.BOOKED;
        this.lockedBy = null;
        this.lockedUntil = null;
    }

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }

    public SeatStatus getStatus() { return status; }
    public void setStatus(SeatStatus status) { this.status = status; }

    public LocalDateTime getLockedUntil() { return lockedUntil; }
    public void setLockedUntil(LocalDateTime lockedUntil) { this.lockedUntil = lockedUntil; }

    public String getLockedBy() { return lockedBy; }
    public void setLockedBy(String lockedBy) { this.lockedBy = lockedBy; }

    public Show getShow() { return show; }
    public void setShow(Show show) { this.show = show; }

    public Seat getSeat() { return seat; }
    public void setSeat(Seat seat) { this.seat = seat; }

    public BookingSeat getBookingSeat() { return bookingSeat; }
    public void setBookingSeat(BookingSeat bookingSeat) { this.bookingSeat = bookingSeat; }
}
