package com.project.theatre_service.dto.seat;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class SeatResponse {

    private Long id;
    private Long screenId;
    private String rowLabel;
    private Integer seatNumber;
    private String seatLabel;
    private String seatType;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}


