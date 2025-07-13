package com.spring.h2.api.Service;

import com.spring.h2.api.model.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BookingService {
    // Booking operations
    Booking createBooking(Long userId, Long showId, List<Long> seatIds);
    Booking confirmBooking(String bookingId, String paymentId);
    void cancelBooking(String bookingId);
    Optional<Booking> getBookingById(String bookingId);
    List<Booking> getUserBookings(Long userId);

    // Seat operations
    List<ShowSeat> getAvailableSeats(Long showId);
    boolean lockSeats(Long showId, List<Long> seatIds, String userEmail);
    void unlockSeats(Long showId, List<Long> seatIds);
    void unlockExpiredSeats();

    // Show operations
    List<Show> getShowsByMovie(Long movieId);
    List<Show> getShowsByCity(Long cityId, LocalDateTime startTime, LocalDateTime endTime);
    List<Show> getUpcomingShows();

    // Utility methods
    void processExpiredBookings();
    boolean isBookingValid(String bookingId);
}
