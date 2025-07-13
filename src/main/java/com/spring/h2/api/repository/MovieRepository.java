package com.spring.h2.api.repository;

import com.spring.h2.api.model.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {
    List<Movie> findByLanguage(String language);
    List<Movie> findByGenre(String genre);
    List<Movie> findByRating(String rating);
    List<Movie> findByReleaseDateAfter(LocalDate date);
    List<Movie> findByTitleContainingIgnoreCase(String title);

    @Query("SELECT m FROM Movie m WHERE m.releaseDate <= :currentDate")
    List<Movie> findCurrentlyRunningMovies(@Param("currentDate") LocalDate currentDate);

    @Query("SELECT DISTINCT m.language FROM Movie m")
    List<String> findAllLanguages();

    @Query("SELECT DISTINCT m.genre FROM Movie m")
    List<String> findAllGenres();
}
