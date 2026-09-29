package com.project.theatre_service.service;

import com.project.theatre_service.dto.seat.CreateSeatRequest;
import com.project.theatre_service.dto.seat.SeatResponse;
import com.project.theatre_service.dto.seat.SeatStatusUpdateRequest;
import com.project.theatre_service.dto.seat.UpdateSeatRequest;
import com.project.theatre_service.entity.screen.Screen;
import com.project.theatre_service.entity.seat.Seat;
import com.project.theatre_service.entity.seat.SeatStatus;
import com.project.theatre_service.mapper.SeatMapper;
import com.project.theatre_service.repository.ScreenRepository;
import com.project.theatre_service.repository.SeatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

@RequiredArgsConstructor
@Service
public class SeatService {
    private final ScreenRepository screenRepository;
    private final SeatRepository seatRepository;
    private final SeatMapper seatMapper;

    public SeatResponse createSeat(Long screenId ,CreateSeatRequest request ){
        Screen screen = screenRepository.findById(screenId).orElseThrow();
        Seat seat = seatMapper.toEntity(request, screen);
        seat.setStatus(SeatStatus.ACTIVE);
        seat.setCreatedAt(LocalDateTime.now(ZoneOffset.UTC));
        seat.setUpdatedAt(LocalDateTime.now(ZoneOffset.UTC));
        Seat createdSeat = seatRepository.save(seat);
        return seatMapper.toResponse(createdSeat);
    }
    public SeatResponse getSeat(Long seatId){
        Seat seat = seatRepository.findById(seatId).orElseThrow();
        return seatMapper.toResponse(seat);
    }

    public List<SeatResponse> getSeatsByScreen(Long id){
        List<Seat> seat = seatRepository.findByScreen_Id(id);
        return seat.stream().map(seatMapper::toResponse).toList();

    }

    public SeatResponse updateSeat(long id , UpdateSeatRequest request){
        Seat seat = seatRepository.findById(id).orElseThrow();
        seatMapper.updateSeatEntity(request, seat);
        seat.setUpdatedAt(LocalDateTime.now(ZoneOffset.UTC));
        Seat updatedSeat = seatRepository.save(seat);
        return seatMapper.toResponse(updatedSeat);

    }

    public SeatResponse updateSeatStatus(Long id, SeatStatusUpdateRequest request){
        Seat seat = seatRepository.findById(id).orElseThrow();
        seat.setStatus(request.getStatus());
        seat.setUpdatedAt(LocalDateTime.now(ZoneOffset.UTC));
        Seat updatedSeat = seatRepository.save(seat);
        return seatMapper.toResponse(updatedSeat);

    }
}
