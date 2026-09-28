package com.project.theatre_service.dto.screen;

import com.project.theatre_service.entity.screen.ScreenType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class UpdateScreenRequest{
    @NotNull
    @Positive
    private Integer screenNumber;
    @Size(max = 100)
    private String name;
    @NotNull
    private ScreenType screenType;
}
