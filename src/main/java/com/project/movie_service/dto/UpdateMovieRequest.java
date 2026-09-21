package com.project.movie_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

@Data
@RequiredArgsConstructor

public class UpdateMovieRequest {

    @NotBlank
    @Size(max = 200)
    private String title;

    private String description;

    @NotNull
    @Positive
    private Integer durationMinutes;

    private LocalDate releaseDate;
}