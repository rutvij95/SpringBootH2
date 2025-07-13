package com.spring.h2.api.repository;

import com.spring.h2.api.model.Show;
import com.spring.h2.api.model.Movie;
import com.spring.h2.api.model.Screen;
import com.spring.h2.api.model.ShowStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ShowRepository extends JpaRepository<Show, Long> {
    List<Show> findByMovie(Movie movie);
    List<Show> findByScreen(Screen screen);
    List<Show> findByMovieId(Long movieId);
    List<Show> findByScreenId(Long screenId);
    List<Show> findByStatus(ShowStatus status);

    @Query("SELECT s FROM Show s WHERE s.movie.id = :movieId AND s.startTime >= :startTime AND s.startTime < :endTime")
    List<Show> findShowsByMovieAndDateRange(@Param("movieId") Long movieId,
                                           @Param("startTime") LocalDateTime startTime,
                                           @Param("endTime") LocalDateTime endTime);

    @Query("SELECT s FROM Show s WHERE s.screen.theater.city.id = :cityId AND s.startTime >= :startTime AND s.startTime < :endTime")
    List<Show> findShowsByCityAndDateRange(@Param("cityId") Long cityId,
                                          @Param("startTime") LocalDateTime startTime,
                                          @Param("endTime") LocalDateTime endTime);

    @Query("SELECT s FROM Show s WHERE s.startTime >= :currentTime AND s.status = :status")
    List<Show> findUpcomingShows(@Param("currentTime") LocalDateTime currentTime,
                                @Param("status") ShowStatus status);
}
