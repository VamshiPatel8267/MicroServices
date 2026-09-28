package com.project.theatre_service.dto.theatre;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;


@Getter
@Setter
public class TheatreResponse {

    private Long id;
    private String name;
    private String address;
    private Long cityId;
    private String cityName;
    private String state;
    private String country;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
