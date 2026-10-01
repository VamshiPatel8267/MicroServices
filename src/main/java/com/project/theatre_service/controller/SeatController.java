package com.project.theatre_service.controller;

import com.project.theatre_service.dto.seat.CreateSeatRequest;
import com.project.theatre_service.dto.seat.SeatResponse;
import com.project.theatre_service.dto.seat.SeatStatusUpdateRequest;
import com.project.theatre_service.dto.seat.UpdateSeatRequest;
import com.project.theatre_service.service.SeatService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/seats")
public class SeatController {

    private final SeatService seatService;

    @PostMapping("/{screenId}")
    public ResponseEntity<SeatResponse> createSeat(@PathVariable Long screenId,
                                                   @Valid @RequestBody CreateSeatRequest request){
        SeatResponse response = seatService.createSeat(screenId,request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("screen/{screenId}")
    public ResponseEntity<List<SeatResponse>> getSeatByScreen(@PathVariable Long screenId){
        List<SeatResponse> response = seatService.getSeatsByScreen(screenId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    @GetMapping("/{seatId}")
    public ResponseEntity<SeatResponse> getSeatById(@PathVariable Long seatId){
        SeatResponse response = seatService.getSeat(seatId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping("/{seatId}")
    public ResponseEntity<SeatResponse> updateSeat(@PathVariable Long seatId,
                                                   @Valid @RequestBody UpdateSeatRequest request){
        SeatResponse response = seatService.updateSeat(seatId, request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PatchMapping("/{seatId}/status")
    public ResponseEntity<SeatResponse> updateSeat(@PathVariable Long seatId,
                                                   @Valid @RequestBody SeatStatusUpdateRequest request){
        SeatResponse response = seatService.updateSeatStatus(seatId, request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
