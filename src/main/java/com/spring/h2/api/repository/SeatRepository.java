package com.spring.h2.api.repository;

import com.spring.h2.api.model.Seat;
import com.spring.h2.api.model.Screen;
import com.spring.h2.api.model.SeatType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SeatRepository extends JpaRepository<Seat, Long> {
    List<Seat> findByScreen(Screen screen);
    List<Seat> findByScreenId(Long screenId);
    List<Seat> findBySeatType(SeatType seatType);
    List<Seat> findByScreenAndSeatType(Screen screen, SeatType seatType);
    Optional<Seat> findByScreenAndSeatNumber(Screen screen, String seatNumber);

    @Query("SELECT s FROM Seat s WHERE s.screen.id = :screenId ORDER BY s.rowNumber, s.columnNumber")
    List<Seat> findSeatsByScreenOrderByRowAndColumn(@Param("screenId") Long screenId);

    @Query("SELECT COUNT(s) FROM Seat s WHERE s.screen.id = :screenId AND s.seatType = :seatType")
    Long countSeatsByScreenAndType(@Param("screenId") Long screenId, @Param("seatType") SeatType seatType);
}
