package com.project.movie_service.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@RequiredArgsConstructor

public class MovieResponse {

    private Long id;

    private String title;

    private String description;


    private Integer durationMinutes;

    private LocalDate releaseDate;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
