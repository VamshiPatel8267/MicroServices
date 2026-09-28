package com.project.theatre_service.mapper;

import com.project.theatre_service.dto.theatre.CreateTheatreRequest;
import com.project.theatre_service.dto.theatre.TheatreResponse;
import com.project.theatre_service.dto.theatre.UpdateTheatreRequest;
import com.project.theatre_service.entity.city.City;
import com.project.theatre_service.entity.theatre.Theatre;

public class TheatreMapper {

    public Theatre toEntity(CreateTheatreRequest request, City city){
        Theatre theatre = new Theatre();
        theatre.setName(request.getName());
        theatre.setAddress(request.getAddress());
        theatre.setCity(city);
        return theatre;
    }

    public void updateEntity(UpdateTheatreRequest request, Theatre theatre , City city){
        theatre.setName(request.getName());
        theatre.setAddress(request.getAddress());
        theatre.setCity(city);
    }

    public TheatreResponse toResponse(Theatre theatre){
        TheatreResponse response = new TheatreResponse();

        response.setId(theatre.getId());
        response.setName(theatre.getName());
        response.setAddress(theatre.getAddress());


        response.setCityId(theatre.getCity().getId());
        response.setCityName(theatre.getCity().getName());
        response.setState(theatre.getCity().getState());
        response.setCountry(theatre.getCity().getCountry());

        response.setStatus(theatre.getStatus().name());
        response.setCreatedAt(theatre.getCreatedAt());
        response.setUpdatedAt(theatre.getUpdatedAt());

        return response;
    }

}
