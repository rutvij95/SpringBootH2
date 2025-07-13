package com.spring.h2.api.repository;

import com.spring.h2.api.model.ShowSeat;
import com.spring.h2.api.model.Show;
import com.spring.h2.api.model.Seat;
import com.spring.h2.api.model.SeatStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ShowSeatRepository extends JpaRepository<ShowSeat, Long> {
    List<ShowSeat> findByShow(Show show);
    List<ShowSeat> findByShowId(Long showId);
    List<ShowSeat> findByShowAndStatus(Show show, SeatStatus status);
    Optional<ShowSeat> findByShowAndSeat(Show show, Seat seat);

    @Query("SELECT ss FROM ShowSeat ss WHERE ss.show.id = :showId AND ss.status = :status")
    List<ShowSeat> findAvailableSeats(@Param("showId") Long showId, @Param("status") SeatStatus status);

    @Query("SELECT ss FROM ShowSeat ss WHERE ss.show.id = :showId AND ss.status = 'LOCKED' AND ss.lockedUntil < :currentTime")
    List<ShowSeat> findExpiredLockedSeats(@Param("showId") Long showId, @Param("currentTime") LocalDateTime currentTime);

    @Modifying
    @Query("UPDATE ShowSeat ss SET ss.status = 'AVAILABLE', ss.lockedBy = null, ss.lockedUntil = null WHERE ss.status = 'LOCKED' AND ss.lockedUntil < :currentTime")
    int unlockExpiredSeats(@Param("currentTime") LocalDateTime currentTime);

    @Query("SELECT COUNT(ss) FROM ShowSeat ss WHERE ss.show.id = :showId AND ss.status = :status")
    Long countSeatsByShowAndStatus(@Param("showId") Long showId, @Param("status") SeatStatus status);
}
