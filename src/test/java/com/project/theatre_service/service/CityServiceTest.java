package com.project.theatre_service.service;

import com.project.theatre_service.dto.city.*;
import com.project.theatre_service.entity.city.*;
import com.project.theatre_service.exception.CityNotFoundException;
import com.project.theatre_service.mapper.CityMapper;
import com.project.theatre_service.repository.CityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CityServiceTest {
    @Mock CityRepository cityRepository;
    @Mock CityMapper cityMapper;
    @InjectMocks CityService cityService;

    City city;
    CreateCityRequest createRequest;
    CityResponse response;

    @BeforeEach void setUp() {
        city = new City(); city.setId(1L); city.setName("Hyderabad");
        city.setState("Telangana"); city.setCountry("India");
        city.setStatus(CityStatus.ACTIVE);
        createRequest = new CreateCityRequest();
        createRequest.setName("Hyderabad"); createRequest.setState("Telangana");
        createRequest.setCountry("India");
        response = new CityResponse(); response.setId(1L); response.setName("Hyderabad");
        response.setStatus("ACTIVE");
    }

    @Test void createCity_savesActiveCityAndReturnsResponse() {
        when(cityMapper.toEntity(createRequest)).thenReturn(city);
        when(cityRepository.save(city)).thenReturn(city);
        when(cityMapper.toResponse(city)).thenReturn(response);
        assertSame(response, cityService.createCity(createRequest));
        assertEquals(CityStatus.ACTIVE, city.getStatus());
        assertNotNull(city.getCreatedAt()); assertNotNull(city.getUpdatedAt());
        assertEquals(city.getCreatedAt(), city.getUpdatedAt());
        verify(cityRepository).save(city);
        verify(cityMapper).toResponse(city);
    }

    @Test void getCity_whenFound_returnsMappedResponse() {
        when(cityRepository.findById(1L)).thenReturn(Optional.of(city));
        when(cityMapper.toResponse(city)).thenReturn(response);
        assertSame(response, cityService.getCity(1L));
        verify(cityMapper).toResponse(city);
    }

    @Test void getCity_whenMissing_throwsCityNotFound() {
        when(cityRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(CityNotFoundException.class, () -> cityService.getCity(99L));
        verify(cityMapper, never()).toResponse(any());
    }

    @Test void getAllCities_returnsMappedCities() {
        City second = new City(); second.setId(2L); second.setStatus(CityStatus.ACTIVE);
        CityResponse secondResponse = new CityResponse(); secondResponse.setId(2L);
        when(cityRepository.findAll()).thenReturn(List.of(city, second));
        when(cityMapper.toResponse(city)).thenReturn(response);
        when(cityMapper.toResponse(second)).thenReturn(secondResponse);
        assertEquals(List.of(response, secondResponse), cityService.getAllCities());
        verify(cityRepository).findAll();
        verify(cityMapper, times(2)).toResponse(any(City.class));
    }

    @Test void getAllCities_whenEmpty_returnsEmptyList() {
        when(cityRepository.findAll()).thenReturn(List.of());
        assertTrue(cityService.getAllCities().isEmpty());
        verifyNoInteractions(cityMapper);
    }

    @Test void updateCityStatus_updatesStatusTimestampAndReturnsResponse() {
        CityStatusUpdateRequest request = new CityStatusUpdateRequest();
        request.setStatus(CityStatus.INACTIVE);
        when(cityRepository.findById(1L)).thenReturn(Optional.of(city));
        when(cityRepository.save(city)).thenReturn(city);
        when(cityMapper.toResponse(city)).thenReturn(response);
        var before = java.time.LocalDateTime.now(java.time.ZoneOffset.UTC).minusSeconds(1);
        assertSame(response, cityService.updateCityStatus(1L, request));
        assertEquals(CityStatus.INACTIVE, city.getStatus());
        assertTrue(city.getUpdatedAt().isAfter(before));
        verify(cityRepository).save(city);
    }

    @Test void updateCityStatus_whenMissing_throwsAndDoesNotSave() {
        CityStatusUpdateRequest request = new CityStatusUpdateRequest();
        request.setStatus(CityStatus.INACTIVE);
        when(cityRepository.findById(7L)).thenReturn(Optional.empty());
        assertThrows(CityNotFoundException.class, () -> cityService.updateCityStatus(7L, request));
        verify(cityRepository, never()).save(any());
    }
}
