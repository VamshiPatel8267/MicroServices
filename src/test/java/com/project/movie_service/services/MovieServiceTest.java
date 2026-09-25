package com.project.movie_service.services;

import com.project.movie_service.dto.CreateMovieRequest;
import com.project.movie_service.dto.MovieResponse;
import com.project.movie_service.dto.UpdateMovieRequest;
import com.project.movie_service.entity.Movie;
import com.project.movie_service.entity.MovieStatus;
import com.project.movie_service.exception.MovieNotFoundException;
import com.project.movie_service.mapper.MovieMapper;
import com.project.movie_service.repository.MovieRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MovieServiceTest {

    @Mock
    private MovieRepository movieRepository;

    @Mock
    private MovieMapper movieMapper;

    @InjectMocks
    private MovieService movieService;

    private Movie movie;
    private MovieResponse response;

    @BeforeEach
    void setUp() {
        movie = new Movie();
        movie.setId(1L);
        movie.setTitle("Interstellar");
        movie.setDescription("A science fiction movie");
        movie.setDurationMinutes(169);
        movie.setReleaseDate(LocalDate.of(2014, 11, 7));
        movie.setStatus(MovieStatus.ACTIVE);
        movie.setCreatedAt(LocalDateTime.of(2026, 1, 1, 10, 0));
        movie.setUpdatedAt(LocalDateTime.of(2026, 1, 1, 10, 0));

        response = new MovieResponse();
        response.setId(1L);
        response.setTitle("Interstellar");
        response.setDescription("A science fiction movie");
        response.setDurationMinutes(169);
        response.setReleaseDate(LocalDate.of(2014, 11, 7));
        response.setStatus("ACTIVE");
        response.setCreatedAt(movie.getCreatedAt());
        response.setUpdatedAt(movie.getUpdatedAt());
    }

    @Test
    void createMovie_shouldSetActiveStatusAndTimestampsAndReturnResponse() {
        CreateMovieRequest request = new CreateMovieRequest();
        request.setTitle("Interstellar");
        request.setDescription("A science fiction movie");
        request.setDurationMinutes(169);
        request.setReleaseDate(LocalDate.of(2014, 11, 7));

        Movie entity = new Movie();
        entity.setTitle(request.getTitle());
        entity.setDurationMinutes(request.getDurationMinutes());

        when(movieMapper.toEntity(request)).thenReturn(entity);
        when(movieRepository.save(entity)).thenAnswer(invocation -> {
            entity.setId(1L);
            return entity;
        });
        when(movieMapper.toResponse(entity)).thenReturn(response);

        MovieResponse actual = movieService.createMovie(request);

        assertSame(response, actual);
        assertEquals(MovieStatus.ACTIVE, entity.getStatus());
        assertNotNull(entity.getCreatedAt());
        assertNotNull(entity.getUpdatedAt());
        assertEquals(entity.getCreatedAt(), entity.getUpdatedAt());
        verify(movieMapper).toEntity(request);
        verify(movieRepository).save(entity);
        verify(movieMapper).toResponse(entity);
    }

    @Test
    void getMovie_whenMovieExists_shouldReturnMappedResponse() {
        when(movieRepository.findById(1L)).thenReturn(Optional.of(movie));
        when(movieMapper.toResponse(movie)).thenReturn(response);

        MovieResponse actual = movieService.getMovie(1L);

        assertSame(response, actual);
        verify(movieRepository).findById(1L);
        verify(movieMapper).toResponse(movie);
    }

    @Test
    void getMovie_whenMovieDoesNotExist_shouldThrowMovieNotFoundException() {
        when(movieRepository.findById(999L)).thenReturn(Optional.empty());

        MovieNotFoundException exception = assertThrows(
                MovieNotFoundException.class,
                () -> movieService.getMovie(999L)
        );

        assertEquals("Movie with id 999 not found", exception.getMessage());
        verify(movieRepository).findById(999L);
        verifyNoInteractions(movieMapper);
    }

    @Test
    void getMovies_withoutStatus_shouldUseFindAllAndMapResults() {
        Pageable pageable = PageRequest.of(0, 20, Sort.by("createdAt").descending());
        Page<Movie> moviePage = new PageImpl<>(List.of(movie), pageable, 1);
        when(movieRepository.findAll(pageable)).thenReturn(moviePage);
        when(movieMapper.toResponse(movie)).thenReturn(response);

        Page<MovieResponse> actual = movieService.getMovies(null, pageable);

        assertEquals(1, actual.getTotalElements());
        assertEquals(1, actual.getContent().size());
        assertSame(response, actual.getContent().get(0));
        verify(movieRepository).findAll(pageable);
        verify(movieRepository, never()).findByStatus(any(), any());
    }

    @Test
    void getMovies_withStatus_shouldUseFindByStatusAndMapResults() {
        Pageable pageable = PageRequest.of(0, 20);
        Page<Movie> moviePage = new PageImpl<>(List.of(movie), pageable, 1);
        when(movieRepository.findByStatus(MovieStatus.ACTIVE, pageable)).thenReturn(moviePage);
        when(movieMapper.toResponse(movie)).thenReturn(response);

        Page<MovieResponse> actual = movieService.getMovies(MovieStatus.ACTIVE, pageable);

        assertEquals(1, actual.getTotalElements());
        assertEquals("ACTIVE", actual.getContent().get(0).getStatus());
        verify(movieRepository).findByStatus(MovieStatus.ACTIVE, pageable);
        verify(movieRepository, never()).findAll(any(Pageable.class));
    }

    @Test
    void getMovies_shouldPreservePaginationMetadata() {
        Pageable pageable = PageRequest.of(2, 10);
        Page<Movie> moviePage = new PageImpl<>(List.of(movie), pageable, 31);
        when(movieRepository.findAll(pageable)).thenReturn(moviePage);
        when(movieMapper.toResponse(movie)).thenReturn(response);

        Page<MovieResponse> actual = movieService.getMovies(null, pageable);

        assertEquals(2, actual.getNumber());
        assertEquals(10, actual.getSize());
        assertEquals(31, actual.getTotalElements());
        assertEquals(4, actual.getTotalPages());
    }

    @Test
    void updateMovie_whenMovieExists_shouldUpdateFieldsAndSave() {
        UpdateMovieRequest request = new UpdateMovieRequest();
        request.setTitle("Interstellar Updated");
        request.setDescription("Updated description");
        request.setDurationMinutes(170);
        request.setReleaseDate(LocalDate.of(2014, 11, 7));

        LocalDateTime originalUpdatedAt = movie.getUpdatedAt();
        when(movieRepository.findById(1L)).thenReturn(Optional.of(movie));
        when(movieRepository.save(movie)).thenReturn(movie);
        when(movieMapper.toResponse(movie)).thenReturn(response);

        MovieResponse actual = movieService.updateMovie(1L, request);

        assertSame(response, actual);
        verify(movieMapper).updateEntity(request, movie);
        verify(movieRepository).save(movie);
        // This assertion intentionally documents the desired contract.
        // It is expected to fail against the current branch until updateMovie updates updatedAt.
        assertNotEquals(originalUpdatedAt, movie.getUpdatedAt(),
                "updatedAt should change when a movie is updated");
    }

    @Test
    void updateMovie_whenMovieDoesNotExist_shouldThrowMovieNotFoundException() {
        UpdateMovieRequest request = new UpdateMovieRequest();
        request.setTitle("Updated");
        request.setDurationMinutes(170);
        when(movieRepository.findById(999L)).thenReturn(Optional.empty());

        MovieNotFoundException exception = assertThrows(
                MovieNotFoundException.class,
                () -> movieService.updateMovie(999L, request)
        );

        assertEquals("Movie with Id 999 NOT Found", exception.getMessage());
        verify(movieRepository).findById(999L);
        verify(movieRepository, never()).save(any());
    }

    @Test
    void updateMovieStatus_shouldChangeStatusAndSave() {
        LocalDateTime originalUpdatedAt = movie.getUpdatedAt();
        when(movieRepository.findById(1L)).thenReturn(Optional.of(movie));
        when(movieRepository.save(movie)).thenReturn(movie);
        when(movieMapper.toResponse(movie)).thenReturn(response);

        MovieResponse actual = movieService.updateMovieStatus(1L, MovieStatus.INACTIVE);

        assertSame(response, actual);
        assertEquals(MovieStatus.INACTIVE, movie.getStatus());
        verify(movieRepository).save(movie);
        // Expected to fail against the current branch until updateMovieStatus updates updatedAt.
        assertNotEquals(originalUpdatedAt, movie.getUpdatedAt(),
                "updatedAt should change when movie status changes");
    }

    @Test
    void updateMovieStatus_whenMovieDoesNotExist_shouldThrowMovieNotFoundException() {
        when(movieRepository.findById(999L)).thenReturn(Optional.empty());

        MovieNotFoundException exception = assertThrows(
                MovieNotFoundException.class,
                () -> movieService.updateMovieStatus(999L, MovieStatus.INACTIVE)
        );

        assertEquals("Movie with Id 999 NOT Found", exception.getMessage());
        verify(movieRepository).findById(999L);
        verify(movieRepository, never()).save(any());
    }

    @Test
    void updateMovie_shouldNotChangeCreatedAt() {
        LocalDateTime originalCreatedAt = movie.getCreatedAt();
        UpdateMovieRequest request = new UpdateMovieRequest();
        request.setTitle("Updated");
        request.setDurationMinutes(170);

        when(movieRepository.findById(1L)).thenReturn(Optional.of(movie));
        when(movieRepository.save(movie)).thenReturn(movie);
        when(movieMapper.toResponse(movie)).thenReturn(response);

        movieService.updateMovie(1L, request);

        assertEquals(originalCreatedAt, movie.getCreatedAt());
    }
}
