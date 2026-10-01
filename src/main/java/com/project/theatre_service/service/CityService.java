package com.project.theatre_service.service;

import com.project.theatre_service.dto.city.CityResponse;
import com.project.theatre_service.dto.city.CityStatusUpdateRequest;
import com.project.theatre_service.dto.city.CreateCityRequest;
import com.project.theatre_service.entity.city.City;
import com.project.theatre_service.entity.city.CityStatus;
import com.project.theatre_service.exception.CityNotFoundException;
import com.project.theatre_service.exception.InactiveCityException;
import com.project.theatre_service.mapper.CityMapper;
import com.project.theatre_service.repository.CityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

@RequiredArgsConstructor
@Service
public class CityService {

    private final CityRepository cityRepository;
    private final CityMapper cityMapper;

    public CityResponse createCity(CreateCityRequest request){
        City city = cityMapper.toEntity(request);
        city.setStatus(CityStatus.ACTIVE);
        if(city.getStatus() == CityStatus.INACTIVE){
            throw new InactiveCityException("City is currently INACTIVE");
        }else{
            LocalDateTime now = LocalDateTime.now(ZoneOffset.UTC);
            city.setCreatedAt(now);
            city.setUpdatedAt(now);
            City response = cityRepository.save(city);
            return cityMapper.toResponse(response);
        }

    }

    public CityResponse getCity(Long id){
        City city = cityRepository.findById(id).orElseThrow(()-> new CityNotFoundException("City not Found with ID: "+ id));
        return cityMapper.toResponse(city);
    }
    public List<CityResponse> getAllCities(){
       List<City> allCities =  cityRepository.findAll();
       return allCities.stream().map(cityMapper::toResponse).toList();
    }

    public CityResponse updateCityStatus(Long id ,CityStatusUpdateRequest request){
        City city = cityRepository.findById(id).orElseThrow(()-> new CityNotFoundException("City not Found with ID: "+ id));
        if (request.getStatus() == null) {
            throw new IllegalArgumentException("City status must not be null");
        }
        city.setStatus(request.getStatus());
        city.setUpdatedAt(LocalDateTime.now(ZoneOffset.UTC));
        City savedCity = cityRepository.save(city);

        return cityMapper.toResponse(savedCity);
    }

}
