package com.spring.h2.api.controller;

import com.spring.h2.api.model.Movie;
import com.spring.h2.api.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/movie")
public class MovieController {

    @Autowired
    private MovieRepository movieRepository;

    // Get all movies
    @GetMapping
    public List<Movie> getAllMovies() {
        return movieRepository.findAll();
    }

    // Get movie by ID
    @GetMapping("/{id}")
    public ResponseEntity<Movie> getMovieById(@PathVariable Long id) {
        return movieRepository.findById(id)
                .map(movie -> ResponseEntity.ok(movie))
                .orElse(ResponseEntity.notFound().build());
    }

    // Add a new movie
    @PostMapping
    public Movie addMovie(@RequestBody Movie movie) {
        return movieRepository.save(movie);
    }

    // Update a movie
    @PutMapping("/{id}")
    public ResponseEntity<Movie> updateMovie(@PathVariable Long id, @RequestBody Movie movieDetails) {
        return movieRepository.findById(id).map(movie -> {
            movie.setMovieName(movieDetails.getMovieName());
            movie.setRating(movieDetails.getRating());
            return ResponseEntity.ok(movieRepository.save(movie));
        }).orElse(ResponseEntity.notFound().build());
    }

    // Delete a movie
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteMovie(@PathVariable Long id) {
        return movieRepository.findById(id).map(movie -> {
            movieRepository.delete(movie);
            return ResponseEntity.noContent().build();
        }).orElse(ResponseEntity.notFound().build());
    }
}