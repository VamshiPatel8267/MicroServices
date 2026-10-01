package com.project.theatre_service.service;

import com.project.theatre_service.dto.theatre.CreateTheatreRequest;
import com.project.theatre_service.dto.theatre.TheatreResponse;
import com.project.theatre_service.dto.theatre.TheatreStatusUpdateRequest;
import com.project.theatre_service.dto.theatre.UpdateTheatreRequest;
import com.project.theatre_service.entity.city.City;
import com.project.theatre_service.entity.city.CityStatus;
import com.project.theatre_service.entity.theatre.Theatre;
import com.project.theatre_service.exception.TheatreNotFoundException;
import com.project.theatre_service.mapper.TheatreMapper;
import com.project.theatre_service.repository.CityRepository;
import com.project.theatre_service.repository.TheatreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

@RequiredArgsConstructor
@Service
public class TheatreService {
    private final TheatreRepository theatreRepository;
    private final CityRepository cityRepository;
    private final TheatreMapper mapper;

    public TheatreResponse createTheatre(CreateTheatreRequest request){
        Long cityId = request.getCityId();
        City city = cityRepository.findById(cityId).orElseThrow();
        Theatre theatre = mapper.toEntity(request,city);
        LocalDateTime now = LocalDateTime.now(ZoneOffset.UTC);
        theatre.setCreatedAt(now);
        theatre.setUpdatedAt(now);
        if(city.getStatus() != CityStatus.ACTIVE){
            return null;
        }else {

            Theatre saved = theatreRepository.save(theatre);
            return mapper.toResponse(saved);
        }
    }

    public TheatreResponse getTheatre(Long id){
        Theatre theatre = theatreRepository.findById(id).orElseThrow(()->new TheatreNotFoundException("Theatre Not Found With ID: "+ id));
        return mapper.toResponse(theatre);
    }

    public List<TheatreResponse> getAllTheatres(){
        List<Theatre> allTheatres = theatreRepository.findAll();
        return allTheatres.stream().map(mapper::toResponse).toList();

    }
    public List<TheatreResponse> getTheatresByCity(Long cityId){
        List<Theatre> allTheatres = theatreRepository.findByCity_Id(cityId);
        return allTheatres.stream().map(mapper::toResponse).toList();
    }

    public TheatreResponse updateTheatre(Long id , UpdateTheatreRequest request){
        Theatre theatre = theatreRepository.findById(id).orElseThrow(()->new TheatreNotFoundException("Theatre Not Found With ID: "+ id));
        City city = theatre.getCity();
        if(city == null || city.getStatus() != CityStatus.ACTIVE){
            throw new TheatreNotFoundException("not found");
        }else{
            mapper.updateEntity(request,theatre, city);
            theatre.setUpdatedAt(LocalDateTime.now(ZoneOffset.UTC));
            Theatre saved = theatreRepository.save(theatre);
            return mapper.toResponse(saved);
        }

    }

    public TheatreResponse updateTheatreStatus(Long id, TheatreStatusUpdateRequest request){
        Theatre theatre = theatreRepository.findById(id).orElseThrow(()->new TheatreNotFoundException("Theatre Not Found With ID: "+ id));
        theatre.setStatus(request.getStatus());
        Theatre updatedStatus = theatreRepository.save(theatre);
        return mapper.toResponse(updatedStatus);
    }
}
