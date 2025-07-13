package com.spring.h2.api.repository;

import com.spring.h2.api.model.Ticket;
import com.spring.h2.api.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
    Optional<Ticket> findByTicketId(String ticketId);
    Optional<Ticket> findByBooking(Booking booking);
    Optional<Ticket> findByBookingId(Long bookingId);
    Optional<Ticket> findByQrCode(String qrCode);
    List<Ticket> findByIsUsed(Boolean isUsed);

    @Query("SELECT t FROM Ticket t WHERE t.booking.user.id = :userId ORDER BY t.generatedAt DESC")
    List<Ticket> findUserTickets(@Param("userId") Long userId);

    @Query("SELECT t FROM Ticket t WHERE t.isUsed = false AND t.booking.show.startTime < :currentTime")
    List<Ticket> findExpiredUnusedTickets(@Param("currentTime") LocalDateTime currentTime);
}
