package com.project.theatre_service.mapper;

import com.project.theatre_service.dto.seat.CreateSeatRequest;
import com.project.theatre_service.dto.seat.SeatResponse;
import com.project.theatre_service.dto.seat.UpdateSeatRequest;
import com.project.theatre_service.entity.screen.Screen;
import com.project.theatre_service.entity.seat.Seat;
import org.springframework.stereotype.Component;


@Component
public class SeatMapper {

    public Seat toEntity(CreateSeatRequest request, Screen screen){
        Seat seat = new Seat();
        seat.setRowLabel(request.getRowLabel());
        seat.setSeatNumber(request.getSeatNumber());
        seat.setSeatType(request.getSeatType());
        seat.setScreen(screen);

        return seat;
    }


    public void updateSeatEntity(UpdateSeatRequest request , Seat seat){
        seat.setRowLabel(request.getRowLabel());
        seat.setSeatNumber(request.getSeatNumber());
        seat.setSeatType(request.getSeatType());
    }

    public SeatResponse toResponse(Seat seat){
        SeatResponse response = new SeatResponse();
        response.setId(seat.getId());
        response.setScreenId(seat.getScreen().getId());
        response.setRowLabel(seat.getRowLabel());
        response.setSeatNumber(seat.getSeatNumber());
        response.setSeatLabel(seat.getRowLabel() + seat.getSeatNumber());
        response.setSeatType(seat.getSeatType().name());
        response.setStatus(seat.getStatus().name());
        response.setCreatedAt(seat.getCreatedAt());
        response.setUpdatedAt(seat.getUpdatedAt());

        return response;
    }
}
