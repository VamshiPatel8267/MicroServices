package com.project.theatre_service.dto.screen;

import com.project.theatre_service.entity.screen.ScreenStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ScreenStatusUpdateRequest {

    @NotNull
    private ScreenStatus status;
}