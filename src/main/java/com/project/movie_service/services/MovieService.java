package com.project.movie_service.services;


import com.project.movie_service.dto.CreateMovieRequest;
import com.project.movie_service.dto.MovieResponse;
import com.project.movie_service.dto.UpdateMovieRequest;
import com.project.movie_service.entity.Movie;
import com.project.movie_service.entity.MovieStatus;
import com.project.movie_service.exception.MovieNotFoundException;
import com.project.movie_service.mapper.MovieMapper;
import com.project.movie_service.repository.MovieRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

@Service
@RequiredArgsConstructor

public class MovieService {


    private final MovieRepository movieRepository;
    private final MovieMapper movieMapper;


    public MovieResponse createMovie(CreateMovieRequest request){
        Movie movie = movieMapper.toEntity(request);
        movie.setStatus(MovieStatus.ACTIVE);
        LocalDateTime now = LocalDateTime.now(ZoneOffset.UTC);
        movie.setCreatedAt(now);
        movie.setUpdatedAt(now);
        Movie createdMovie =  movieRepository.save(movie);
        return movieMapper.toResponse(createdMovie);
    }

    public MovieResponse getMovie(Long id){
        Movie movie = movieRepository.findById(id).orElseThrow(()-> new MovieNotFoundException(String.format("Movie with id %d not found", id)));
        MovieResponse response = movieMapper.toResponse(movie);
        return response;
    }

    public Page<MovieResponse> getMovies(MovieStatus status,Pageable pageable){
        Page<Movie> movies;

        if(status != null){
            movies = movieRepository.findByStatus(status, pageable);
            return movies.map(movieMapper::toResponse);
        }else{
            movies = movieRepository.findAll(pageable);
            return movies.map(movieMapper::toResponse);
        }

    }

    public MovieResponse updateMovie(Long id , UpdateMovieRequest request){
        Movie movie = movieRepository.findById(id).orElseThrow(()-> new MovieNotFoundException(String.format("Movie with Id %d NOT Found", id)));
        movieMapper.updateEntity(request,movie);
        Movie updatedMovie = movieRepository.save(movie);
        return movieMapper.toResponse(updatedMovie);
    }

    public MovieResponse updateMovieStatus(Long id , MovieStatus status){
        Movie movie = movieRepository.findById(id).orElseThrow(()-> new MovieNotFoundException(String.format("Movie with Id %d NOT Found", id)));
        movie.setStatus(status);
        Movie savedMovie = movieRepository.save(movie);
        return movieMapper.toResponse(savedMovie);
    }


}
