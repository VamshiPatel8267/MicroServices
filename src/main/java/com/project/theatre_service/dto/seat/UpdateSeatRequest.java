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
public class UpdateSeatRequest {
    @NotBlank
    @Size(max = 10)
    private String rowLabel;
    @NotNull
    @Positive
    private Integer seatNumber;
    @NotNull
    private SeatType seatType;
}

/*
* | Field      | Type       | Validation                     |
| ---------- | ---------- | ------------------------------ |
| rowLabel   | `String`   | `@NotBlank`, `@Size(max = 10)` |
| seatNumber | `Integer`  | `@NotNull`, `@Positive`        |
| seatType   | `SeatType` | `@NotNull`                     |
*/