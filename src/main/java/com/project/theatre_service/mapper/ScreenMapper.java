package com.project.theatre_service.mapper;

import com.project.theatre_service.dto.screen.CreateScreenRequest;
import com.project.theatre_service.dto.screen.UpdateScreenRequest;
import com.project.theatre_service.entity.screen.Screen;
import com.project.theatre_service.entity.theatre.Theatre;

public class ScreenMapper {

    public Screen toEntity(CreateScreenRequest request, Theatre theatre){
        Screen screen = new Screen();

        screen.setScreenNumber(request.getScreenNumber());
        screen.setName(request.getName());
        screen.setScreenType(request.getScreenType());
        screen.setTheatre(theatre);

        return screen;
    }
    public void updateEnity(UpdateScreenRequest request, Screen screen){
        screen.setName(request.getName());
        screen.setScreenNumber(request.getScreenNumber());
        screen.setScreenType(request.getScreenType());

    }
}
