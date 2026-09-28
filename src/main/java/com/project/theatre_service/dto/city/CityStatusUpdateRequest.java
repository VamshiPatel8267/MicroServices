package com.project.theatre_service.dto.city;

import com.project.theatre_service.entity.city.CityStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CityStatusUpdateRequest {
    @NotNull
    private CityStatus status;
}
