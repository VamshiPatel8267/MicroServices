package com.project.theatre_service.controller;

import com.project.theatre_service.dto.screen.CreateScreenRequest;
import com.project.theatre_service.dto.screen.ScreenResponse;
import com.project.theatre_service.dto.screen.ScreenStatusUpdateRequest;
import com.project.theatre_service.dto.screen.UpdateScreenRequest;
import com.project.theatre_service.service.ScreenService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/screen")
public class ScreenController {

    private final ScreenService screenService;

    @PostMapping("/{theatreId}")
    public ResponseEntity<ScreenResponse> create(@PathVariable Long theatreId,
                                                 @Valid @RequestBody CreateScreenRequest request){
       ScreenResponse response =  screenService.createScreen(theatreId, request);
       return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("theatre/{theatreId}")
    public ResponseEntity<List<ScreenResponse>> getScreenByTheatreId(@PathVariable Long theatreId){
        List<ScreenResponse> response = screenService.getScreenByTheatres(theatreId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    @GetMapping("/{screenId}")
    public ResponseEntity<ScreenResponse> getScreenById(@PathVariable Long screenId){
       ScreenResponse response = screenService.getScreen(screenId);
       return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    @PutMapping("/{screenId}")
    public ResponseEntity<ScreenResponse> updateScreen(@PathVariable Long screenId ,
                                                       @Valid @RequestBody UpdateScreenRequest request){
        ScreenResponse response = screenService.updateScreen(screenId,request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PatchMapping("/{screenId}/status")
    public ResponseEntity<ScreenResponse> updateStatus(@PathVariable Long screenId,
                                                       @Valid @RequestBody ScreenStatusUpdateRequest request){
        ScreenResponse response = screenService.updateScreenStatus(screenId, request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
