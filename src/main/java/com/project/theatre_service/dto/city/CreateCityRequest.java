package com.project.theatre_service.dto.city;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class CreateCityRequest {
    @NotBlank
    @Size(max = 100)
    private String name;
    @NotBlank
    @Size(max = 100)
    private String state;
    @NotBlank
    @Size(max = 100)
    private String country;
}
