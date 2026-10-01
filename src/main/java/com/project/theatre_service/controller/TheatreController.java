package com.project.theatre_service.controller;

import com.project.theatre_service.dto.theatre.CreateTheatreRequest;
import com.project.theatre_service.dto.theatre.TheatreResponse;
import com.project.theatre_service.dto.theatre.TheatreStatusUpdateRequest;
import com.project.theatre_service.dto.theatre.UpdateTheatreRequest;
import com.project.theatre_service.service.TheatreService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/theatres")
public class TheatreController {

    private final TheatreService theatreService;

    @PostMapping
    public ResponseEntity<TheatreResponse> createTheatre(@Valid @RequestBody CreateTheatreRequest request){
        TheatreResponse response = theatreService.createTheatre(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<TheatreResponse>> getTheatres(){
        List<TheatreResponse> response = theatreService.getAllTheatres();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/{theatreId}")
    public ResponseEntity<TheatreResponse> getById(@PathVariable Long theatreId){
        TheatreResponse response = theatreService.getTheatre(theatreId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/cities/{cityId}")
    public ResponseEntity<List<TheatreResponse>> getTheatreByCityId(@PathVariable Long cityId){
        List<TheatreResponse> response = theatreService.getTheatresByCity(cityId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping("/{theatreId}")
    public ResponseEntity<TheatreResponse> updateTheatre(@PathVariable Long theatreId,
                                                         @Valid @RequestBody UpdateTheatreRequest request){
        TheatreResponse response = theatreService.updateTheatre(theatreId, request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PatchMapping("/{theatreId}/status")
    public ResponseEntity<TheatreResponse> updateStatus(@PathVariable Long theatreId ,
                                                        @Valid @RequestBody TheatreStatusUpdateRequest request){
        TheatreResponse response = theatreService.updateTheatreStatus(theatreId, request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
