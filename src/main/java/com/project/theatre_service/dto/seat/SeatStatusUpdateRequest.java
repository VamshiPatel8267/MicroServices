package com.project.theatre_service.dto.seat;

import com.project.theatre_service.entity.seat.SeatStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SeatStatusUpdateRequest {
    @NotNull
    private SeatStatus status;
}
