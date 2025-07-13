package com.spring.h2.api.repository;

import com.spring.h2.api.model.BookingSeat;
import com.spring.h2.api.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingSeatRepository extends JpaRepository<BookingSeat, Long> {
    List<BookingSeat> findByBooking(Booking booking);
    List<BookingSeat> findByBookingId(Long bookingId);

    @Query("SELECT bs FROM BookingSeat bs WHERE bs.booking.id = :bookingId")
    List<BookingSeat> findSeatsByBookingId(@Param("bookingId") Long bookingId);

    @Query("SELECT COUNT(bs) FROM BookingSeat bs WHERE bs.booking.id = :bookingId")
    Long countSeatsByBookingId(@Param("bookingId") Long bookingId);
}
