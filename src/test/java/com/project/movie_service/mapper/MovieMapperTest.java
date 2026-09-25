package com.project.movie_service.mapper;

import com.project.movie_service.dto.CreateMovieRequest;
import com.project.movie_service.dto.MovieResponse;
import com.project.movie_service.dto.UpdateMovieRequest;
import com.project.movie_service.entity.Movie;
import com.project.movie_service.entity.MovieStatus;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class MovieMapperTest {

    private final MovieMapper mapper = new MovieMapper();

    @Test
    void toEntity_shouldCopyCreateRequestFields() {
        CreateMovieRequest request = new CreateMovieRequest();
        request.setTitle("Interstellar");
        request.setDescription("Sci-fi");
        request.setDurationMinutes(169);
        request.setReleaseDate(LocalDate.of(2014, 11, 7));

        Movie movie = mapper.toEntity(request);

        assertEquals(request.getTitle(), movie.getTitle());
        assertEquals(request.getDescription(), movie.getDescription());
        assertEquals(request.getDurationMinutes(), movie.getDurationMinutes());
        assertEquals(request.getReleaseDate(), movie.getReleaseDate());
        assertNull(movie.getId());
        assertNull(movie.getStatus());
        assertNull(movie.getCreatedAt());
        assertNull(movie.getUpdatedAt());
    }

    @Test
    void updateEntity_shouldCopyUpdateFieldsWithoutChangingIdentityFields() {
        Movie movie = new Movie();
        movie.setId(1L);
        movie.setStatus(MovieStatus.ACTIVE);
        movie.setCreatedAt(LocalDateTime.of(2026, 1, 1, 10, 0));

        UpdateMovieRequest request = new UpdateMovieRequest();
        request.setTitle("Updated");
        request.setDescription("Updated description");
        request.setDurationMinutes(180);
        request.setReleaseDate(LocalDate.of(2026, 1, 1));

        mapper.updateEntity(request, movie);

        assertEquals("Updated", movie.getTitle());
        assertEquals("Updated description", movie.getDescription());
        assertEquals(180, movie.getDurationMinutes());
        assertEquals(LocalDate.of(2026, 1, 1), movie.getReleaseDate());
        assertEquals(1L, movie.getId());
        assertEquals(MovieStatus.ACTIVE, movie.getStatus());
        assertNotNull(movie.getCreatedAt());
    }

    @Test
    void toResponse_shouldCopyAllResponseFields() {
        Movie movie = new Movie();
        movie.setId(1L);
        movie.setTitle("Interstellar");
        movie.setDescription("Sci-fi");
        movie.setDurationMinutes(169);
        movie.setReleaseDate(LocalDate.of(2014, 11, 7));
        movie.setStatus(MovieStatus.ACTIVE);
        movie.setCreatedAt(LocalDateTime.of(2026, 1, 1, 10, 0));
        movie.setUpdatedAt(LocalDateTime.of(2026, 1, 2, 10, 0));

        MovieResponse response = mapper.toResponse(movie);

        assertEquals(1L, response.getId());
        assertEquals("Interstellar", response.getTitle());
        assertEquals("Sci-fi", response.getDescription());
        assertEquals(169, response.getDurationMinutes());
        assertEquals(LocalDate.of(2014, 11, 7), response.getReleaseDate());
        assertEquals("ACTIVE", response.getStatus());
        assertEquals(movie.getCreatedAt(), response.getCreatedAt());
        assertEquals(movie.getUpdatedAt(), response.getUpdatedAt());
    }
}
