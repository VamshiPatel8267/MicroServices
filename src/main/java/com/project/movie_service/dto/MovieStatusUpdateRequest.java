package com.project.movie_service.dto;

import com.project.movie_service.entity.MovieStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data

@RequiredArgsConstructor
public class MovieStatusUpdateRequest {
    @NotNull
    private MovieStatus status;

}
