package com.spring.h2.api.repository;

import com.spring.h2.api.model.Booking;
import com.spring.h2.api.model.BookingStatus;
import com.spring.h2.api.model.User;
import com.spring.h2.api.model.Show;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    Optional<Booking> findByBookingId(String bookingId);
    List<Booking> findByUser(User user);
    List<Booking> findByUserId(Long userId);
    List<Booking> findByShow(Show show);
    List<Booking> findByStatus(BookingStatus status);
    List<Booking> findByUserAndStatus(User user, BookingStatus status);

    @Query("SELECT b FROM Booking b WHERE b.user.id = :userId ORDER BY b.bookingTime DESC")
    List<Booking> findUserBookingHistory(@Param("userId") Long userId);

    @Query("SELECT b FROM Booking b WHERE b.status = 'PENDING' AND b.bookingTime < :expiryTime")
    List<Booking> findExpiredPendingBookings(@Param("expiryTime") LocalDateTime expiryTime);

    @Query("SELECT COUNT(b) FROM Booking b WHERE b.show.id = :showId AND b.status = 'CONFIRMED'")
    Long countConfirmedBookingsByShow(@Param("showId") Long showId);
}
