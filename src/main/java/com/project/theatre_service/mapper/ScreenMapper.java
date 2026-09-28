package com.project.theatre_service.mapper;

import com.project.theatre_service.dto.screen.CreateScreenRequest;
import com.project.theatre_service.dto.screen.ScreenResponse;
import com.project.theatre_service.dto.screen.UpdateScreenRequest;
import com.project.theatre_service.entity.screen.Screen;
import com.project.theatre_service.entity.theatre.Theatre;
import org.springframework.stereotype.Component;


@Component
public class ScreenMapper {

    public Screen toEntity(CreateScreenRequest request, Theatre theatre){
        Screen screen = new Screen();

        screen.setScreenNumber(request.getScreenNumber());
        screen.setName(request.getName());
        screen.setScreenType(request.getScreenType());
        screen.setTheatre(theatre);

        return screen;
    }
    public void updateEntity(UpdateScreenRequest request, Screen screen){
        screen.setName(request.getName());
        screen.setScreenNumber(request.getScreenNumber());
        screen.setScreenType(request.getScreenType());

    }

    public ScreenResponse toResponse(Screen screen){
        ScreenResponse response = new ScreenResponse();
        response.setId(screen.getId());
        response.setTheatreId(screen.getTheatre().getId());
        response.setScreenNumber(screen.getScreenNumber());
        response.setName(screen.getName());
        response.setScreenType(screen.getScreenType().name());
        response.setStatus(screen.getStatus().name());
        response.setCreatedAt(screen.getCreatedAt());
        response.setUpdatedAt(screen.getUpdatedAt());

        return response;
    }
}
