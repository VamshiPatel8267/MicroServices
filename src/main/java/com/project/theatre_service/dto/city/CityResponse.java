package com.project.theatre_service.dto.city;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CityResponse {
    private Long id;

    private  String name;

    private String state;

    private String country;


    private String status;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
