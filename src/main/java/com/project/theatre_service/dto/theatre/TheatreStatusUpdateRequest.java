package com.project.theatre_service.dto.theatre;

import com.project.theatre_service.entity.theatre.TheatreStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TheatreStatusUpdateRequest {
    @NotNull
    private TheatreStatus status;
}
