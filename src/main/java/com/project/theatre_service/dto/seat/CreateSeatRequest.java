package com.project.theatre_service.dto.seat;

import com.project.theatre_service.entity.seat.SeatType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateSeatRequest {

    @NotBlank
    @Size(max = 10)
    private String rowLabel;
    @NotNull
    @Positive
    private Integer seatNumber;
    @NotNull
    private SeatType seatType;
}


