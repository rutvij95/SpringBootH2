package com.spring.h2.api.serviceImpl;

import com.spring.h2.api.Service.MovieService;
import com.spring.h2.api.model.Movie;
import com.spring.h2.api.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class MovieServiceImpl implements MovieService {

    @Autowired
    private MovieRepository movieRepository;

    @Override
    @Transactional
    public Movie createMovie(Movie movie) {
        if (movie.getReleaseDate() == null) {
            movie.setReleaseDate(LocalDate.now());
        }
        return movieRepository.save(movie);
    }

    @Override
    @Transactional
    public Movie updateMovie(Long id, Movie movie) {
        Movie existingMovie = movieRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Movie not found"));

        existingMovie.setTitle(movie.getTitle());
        existingMovie.setDescription(movie.getDescription());
        existingMovie.setDurationInMinutes(movie.getDurationInMinutes());
        existingMovie.setLanguage(movie.getLanguage());
        existingMovie.setGenre(movie.getGenre());
        existingMovie.setRating(movie.getRating());
        existingMovie.setReleaseDate(movie.getReleaseDate());
        existingMovie.setDirector(movie.getDirector());
        existingMovie.setCast(movie.getCast());
        existingMovie.setPosterUrl(movie.getPosterUrl());

        return movieRepository.save(existingMovie);
    }

    @Override
    @Transactional
    public void deleteMovie(Long id) {
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Movie not found"));
        movieRepository.delete(movie);
    }

    @Override
    public Optional<Movie> getMovieById(Long id) {
        return movieRepository.findById(id);
    }

    @Override
    public List<Movie> getAllMovies() {
        return movieRepository.findAll();
    }

    @Override
    public List<Movie> getMoviesByLanguage(String language) {
        return movieRepository.findByLanguage(language);
    }

    @Override
    public List<Movie> getMoviesByGenre(String genre) {
        return movieRepository.findByGenre(genre);
    }

    @Override
    public List<Movie> getMoviesByRating(String rating) {
        return movieRepository.findByRating(rating);
    }

    @Override
    public List<Movie> getCurrentlyRunningMovies() {
        return movieRepository.findCurrentlyRunningMovies(LocalDate.now());
    }

    @Override
    public List<Movie> searchMoviesByTitle(String title) {
        return movieRepository.findByTitleContainingIgnoreCase(title);
    }

    @Override
    public List<String> getAllLanguages() {
        return movieRepository.findAllLanguages();
    }

    @Override
    public List<String> getAllGenres() {
        return movieRepository.findAllGenres();
    }

    @Override
    public List<Movie> getMoviesReleasedAfter(LocalDate date) {
        return movieRepository.findByReleaseDateAfter(date);
    }
}
