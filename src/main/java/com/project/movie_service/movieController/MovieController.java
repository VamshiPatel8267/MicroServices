package com.project.movie_service.movieController;

import com.project.movie_service.dto.CreateMovieRequest;
import com.project.movie_service.dto.MovieResponse;
import com.project.movie_service.dto.MovieStatusUpdateRequest;
import com.project.movie_service.dto.UpdateMovieRequest;
import com.project.movie_service.entity.MovieStatus;
import com.project.movie_service.exception.InvalidPaginationException;
import com.project.movie_service.services.MovieService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("/api/v1/movies")
@RequiredArgsConstructor
public class MovieController {

    private final MovieService movieService;

    @PostMapping
    public ResponseEntity<MovieResponse> createMovie(
            @Valid @RequestBody CreateMovieRequest request) {

        MovieResponse response = movieService.createMovie(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{movieId}")
    public ResponseEntity<MovieResponse> getMovie(
            @PathVariable Long movieId) {

        MovieResponse response = movieService.getMovie(movieId);

        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<Page<MovieResponse>> getMovies(@RequestParam(required = false) MovieStatus status, @PageableDefault(size = 20) Pageable pageable) {
        if (pageable.getPageSize() > 100) {
            throw new InvalidPaginationException("Page cannot exceed 100");
        } else{
            Page<MovieResponse> responses = movieService.getMovies(status, pageable);
            return ResponseEntity.ok(responses);
        }

    }

    @PutMapping("/{movieId}")
    public ResponseEntity<MovieResponse> updateMovie(
            @PathVariable Long movieId,
            @Valid @RequestBody UpdateMovieRequest request) {

        MovieResponse response =
                movieService.updateMovie(movieId, request);

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{movieId}/status")
    public ResponseEntity<MovieResponse> updateMovieStatus(
            @PathVariable Long movieId,
            @Valid @RequestBody MovieStatusUpdateRequest request) {

        MovieResponse response =
                movieService.updateMovieStatus(movieId, request.getStatus());

        return ResponseEntity.ok(response);
    }
}