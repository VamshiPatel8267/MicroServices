package com.project.theatre_service.dto.screen;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class ScreenResponse {

    private Long id;
    private Long theatreId;
    private Integer screenNumber;
    private String name;
    private String screenType;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}


