package com.project.movie_service.mapper;

import com.project.movie_service.dto.CreateMovieRequest;
import com.project.movie_service.dto.MovieResponse;
import com.project.movie_service.dto.UpdateMovieRequest;
import com.project.movie_service.entity.Movie;
import org.springframework.stereotype.Component;

@Component
public class MovieMapper {
    public Movie toEntity(CreateMovieRequest request){
        Movie movie = new Movie();
        movie.setTitle(request.getTitle());
        movie.setDescription(request.getDescription());
        movie.setDurationMinutes(request.getDurationMinutes());
        movie.setReleaseDate(request.getReleaseDate());
        return movie;
    }

    public  void updateEntity(UpdateMovieRequest request, Movie movie){

        movie.setTitle(request.getTitle());
        movie.setDescription(request.getDescription());
        movie.setDurationMinutes(request.getDurationMinutes());
        movie.setReleaseDate(request.getReleaseDate());

    }

    public MovieResponse toResponse(Movie movie){
        MovieResponse response = new MovieResponse();
        response.setId(movie.getId());
        response.setTitle(movie.getTitle());
        response.setDescription(movie.getDescription());
        response.setDurationMinutes(movie.getDurationMinutes());
        response.setReleaseDate(movie.getReleaseDate());
        response.setStatus(movie.getStatus().name());
        response.setCreatedAt(movie.getCreatedAt());
        response.setUpdatedAt(movie.getUpdatedAt());
        return response;


    }
}
