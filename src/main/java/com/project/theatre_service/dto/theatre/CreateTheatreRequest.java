package com.project.theatre_service.dto.theatre;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateTheatreRequest {
    @NotBlank
    @Size(max = 200)
    private String name;
    @NotBlank
    @Size(max = 500)
    private String address;
    @NotNull
    @Positive
    private Long cityId;
}
