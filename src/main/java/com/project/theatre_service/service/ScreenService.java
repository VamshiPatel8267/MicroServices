package com.project.theatre_service.service;

import com.project.theatre_service.dto.screen.CreateScreenRequest;
import com.project.theatre_service.dto.screen.ScreenResponse;
import com.project.theatre_service.dto.screen.ScreenStatusUpdateRequest;
import com.project.theatre_service.dto.screen.UpdateScreenRequest;
import com.project.theatre_service.entity.screen.Screen;
import com.project.theatre_service.entity.screen.ScreenStatus;
import com.project.theatre_service.entity.theatre.Theatre;
import com.project.theatre_service.entity.theatre.TheatreStatus;
import com.project.theatre_service.exception.InactiveTheatreException;
import com.project.theatre_service.mapper.ScreenMapper;
import com.project.theatre_service.exception.TheatreNotFoundException;
import com.project.theatre_service.exception.ScreenNotFoundException;
import com.project.theatre_service.repository.ScreenRepository;
import com.project.theatre_service.repository.TheatreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ScreenService {

    public final ScreenRepository screenRepository;
    public final TheatreRepository theatreRepository;
    public final ScreenMapper screenMapper;

    public ScreenResponse createScreen(Long theatreId , CreateScreenRequest request){

        Theatre theatre = theatreRepository.findById(theatreId).orElseThrow(() -> new TheatreNotFoundException("Theatre not found with ID: " + theatreId));
        if(theatre.getStatus() == TheatreStatus.INACTIVE ){
            throw new InactiveTheatreException("Theatre is INACTIVE");

        }else{
            Screen screen = screenMapper.toEntity(request, theatre);
            screen.setStatus(ScreenStatus.ACTIVE);
            screen.setCreatedAt(LocalDateTime.now(ZoneOffset.UTC));
            screen.setUpdatedAt(LocalDateTime.now(ZoneOffset.UTC));

            Screen savedScreen = screenRepository.save(screen);

            return screenMapper.toResponse(savedScreen);
        }

    }


    public ScreenResponse getScreen(Long id){
        Screen screen=screenRepository.findById(id).orElseThrow(() -> new ScreenNotFoundException("Screen not found with ID: " + id));
        return screenMapper.toResponse(screen);
    }

    public List<ScreenResponse> getScreenByTheatres(Long theatreId){
        List<Screen> screens = screenRepository.findByTheatre_Id(theatreId);
        return screens.stream().map(screenMapper::toResponse).toList();
    }

    public ScreenResponse updateScreen(Long id, UpdateScreenRequest request){
        Screen screen = screenRepository.findById(id).orElseThrow(() -> new ScreenNotFoundException("Screen not found with ID: " + id));
        screenMapper.updateEntity(request, screen);
        screen.setUpdatedAt(LocalDateTime.now(ZoneOffset.UTC));
        Screen savedScreen = screenRepository.save(screen);
        return screenMapper.toResponse(savedScreen);

    }
    public ScreenResponse updateScreenStatus(
            Long screenId,
            ScreenStatusUpdateRequest request) {

        Screen screen = screenRepository.findById(screenId)
                .orElseThrow(() -> new ScreenNotFoundException("Screen not found with ID: " + screenId));

        screen.setStatus(request.getStatus());
        screen.setUpdatedAt(LocalDateTime.now(ZoneOffset.UTC));

        Screen updatedScreen = screenRepository.save(screen);

        return screenMapper.toResponse(updatedScreen);
    }
}
