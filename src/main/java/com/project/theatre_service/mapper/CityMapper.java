package com.project.theatre_service.mapper;

import com.project.theatre_service.dto.city.CityResponse;
import com.project.theatre_service.dto.city.CreateCityRequest;
import com.project.theatre_service.entity.city.City;
import com.project.theatre_service.entity.city.CityStatus;

public class CityMapper {

    public City toEntity(CreateCityRequest request){
        City city = new City();
        city.setName(request.getName());
        city.setState(request.getState());
        city.setCountry(request.getCountry());
        return city;

    }

    public CityResponse toResponse(City city){
        CityResponse response = new CityResponse();
        response.setId(city.getId());
        response.setName(city.getName());
        response.setStatus(city.getStatus().name());
        response.setCountry(city.getCountry());
        response.setCreatedAt(city.getCreatedAt());
        response.setUpdatedAt(city.getUpdatedAt());

        return response;
    }

    public City updateStatus(CityStatus status){
       City city = new City();
       city.setStatus(status);
        return city;
    }
}
