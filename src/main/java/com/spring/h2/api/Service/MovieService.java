package com.spring.h2.api.Service;

import com.spring.h2.api.model.Movie;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface MovieService {
    Movie createMovie(Movie movie);
    Movie updateMovie(Long id, Movie movie);
    void deleteMovie(Long id);
    Optional<Movie> getMovieById(Long id);
    List<Movie> getAllMovies();
    List<Movie> getMoviesByLanguage(String language);
    List<Movie> getMoviesByGenre(String genre);
    List<Movie> getMoviesByRating(String rating);
    List<Movie> getCurrentlyRunningMovies();
    List<Movie> searchMoviesByTitle(String title);
    List<String> getAllLanguages();
    List<String> getAllGenres();
    List<Movie> getMoviesReleasedAfter(LocalDate date);
}
