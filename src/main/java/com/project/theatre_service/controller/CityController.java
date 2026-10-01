package com.project.theatre_service.controller;

import com.project.theatre_service.dto.city.CityResponse;
import com.project.theatre_service.dto.city.CityStatusUpdateRequest;
import com.project.theatre_service.dto.city.CreateCityRequest;
import com.project.theatre_service.entity.city.City;
import com.project.theatre_service.service.CityService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/cities")
public class CityController {

    private final CityService cityService;

    @PostMapping
    public ResponseEntity<CityResponse> createCity(@Valid @RequestBody CreateCityRequest request){
        CityResponse city = cityService.createCity(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(city);

    }

    @GetMapping
    public ResponseEntity<List<CityResponse>> getAllCities(){
        List<CityResponse> cityResponse = cityService.getAllCities();
        return ResponseEntity.status(HttpStatus.OK).body(cityResponse);
    }
    @GetMapping("/{id}")
    public ResponseEntity<CityResponse> getCityById(@PathVariable Long id){

        CityResponse response = cityService.getCity(id);
        return ResponseEntity.status(HttpStatus.OK).body(response);

    }

    @PatchMapping("/{cityId}/status")
    public ResponseEntity<CityResponse> updateCityStatus(@PathVariable Long cityId,@Valid @RequestBody CityStatusUpdateRequest request){
        CityResponse response = cityService.updateCityStatus(cityId, request);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);

    }

}
