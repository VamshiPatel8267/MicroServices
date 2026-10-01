package com.project.theatre_service.service;

import com.project.theatre_service.dto.theatre.*;
import com.project.theatre_service.entity.city.*;
import com.project.theatre_service.entity.theatre.*;
import com.project.theatre_service.exception.TheatreNotFoundException;
import com.project.theatre_service.exception.CityNotFoundException;
import com.project.theatre_service.exception.InactiveCityException;
import com.project.theatre_service.mapper.TheatreMapper;
import com.project.theatre_service.repository.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TheatreServiceTest {
    @Mock TheatreRepository theatreRepository;
    @Mock CityRepository cityRepository;
    @Mock TheatreMapper mapper;
    @InjectMocks TheatreService service;
    City city; Theatre theatre; TheatreResponse response;

    @BeforeEach void setUp() {
        city = new City(); city.setId(10L); city.setName("Hyderabad");
        city.setState("Telangana"); city.setCountry("India"); city.setStatus(CityStatus.ACTIVE);
        theatre = new Theatre(); theatre.setId(1L); theatre.setName("PVR");
        theatre.setAddress("Madhapur"); theatre.setCity(city); theatre.setStatus(TheatreStatus.ACTIVE);
        response = new TheatreResponse(); response.setId(1L); response.setName("PVR");
    }

    @Test void createTheatre_withActiveCity_savesAndReturnsResponse() {
        CreateTheatreRequest req = new CreateTheatreRequest(); req.setCityId(10L);
        when(cityRepository.findById(10L)).thenReturn(Optional.of(city));
        when(mapper.toEntity(req, city)).thenReturn(theatre);
        when(theatreRepository.save(theatre)).thenReturn(theatre);
        when(mapper.toResponse(theatre)).thenReturn(response);
        assertSame(response, service.createTheatre(req));
        assertNotNull(theatre.getCreatedAt()); assertNotNull(theatre.getUpdatedAt());
        verify(theatreRepository).save(theatre);
    }

    @Test void createTheatre_withInactiveCity_throwsInactiveCityExceptionAndDoesNotSave() {
        CreateTheatreRequest req = new CreateTheatreRequest(); req.setCityId(10L);
        city.setStatus(CityStatus.INACTIVE);
        when(cityRepository.findById(10L)).thenReturn(Optional.of(city));
        when(mapper.toEntity(req, city)).thenReturn(theatre);
        assertThrows(InactiveCityException.class, () -> service.createTheatre(req));
        verify(theatreRepository, never()).save(any());
        verify(mapper, never()).toResponse(any());
    }

    @Test void createTheatre_whenCityMissing_throwsCityNotFoundException() {
        CreateTheatreRequest req = new CreateTheatreRequest(); req.setCityId(77L);
        when(cityRepository.findById(77L)).thenReturn(Optional.empty());
        assertThrows(CityNotFoundException.class, () -> service.createTheatre(req));
        verifyNoInteractions(theatreRepository);
    }

    @Test void getTheatre_returnsMappedTheatre() {
        when(theatreRepository.findById(1L)).thenReturn(Optional.of(theatre));
        when(mapper.toResponse(theatre)).thenReturn(response);
        assertSame(response, service.getTheatre(1L));
    }

    @Test void getTheatre_whenMissing_throwsTheatreNotFound() {
        when(theatreRepository.findById(5L)).thenReturn(Optional.empty());
        assertThrows(TheatreNotFoundException.class, () -> service.getTheatre(5L));
    }

    @Test void getAllTheatres_mapsAll() {
        when(theatreRepository.findAll()).thenReturn(List.of(theatre));
        when(mapper.toResponse(theatre)).thenReturn(response);
        assertEquals(List.of(response), service.getAllTheatres());
    }

    @Test void getTheatresByCity_mapsRepositoryResults() {
        when(theatreRepository.findByCity_Id(10L)).thenReturn(List.of(theatre));
        when(mapper.toResponse(theatre)).thenReturn(response);
        assertEquals(List.of(response), service.getTheatresByCity(10L));
        verify(theatreRepository).findByCity_Id(10L);
    }

    @Test void updateTheatre_updatesExistingCityAndSaves() {
        UpdateTheatreRequest req = new UpdateTheatreRequest();
        when(theatreRepository.findById(1L)).thenReturn(Optional.of(theatre));
        when(theatreRepository.save(theatre)).thenReturn(theatre);
        when(mapper.toResponse(theatre)).thenReturn(response);
        assertSame(response, service.updateTheatre(1L, req));
        verify(mapper).updateEntity(req, theatre, city);
        verify(theatreRepository).save(theatre);
        assertNotNull(theatre.getUpdatedAt());
    }

    @Test void updateTheatre_whenMissing_throwsTheatreNotFound() {
        when(theatreRepository.findById(2L)).thenReturn(Optional.empty());
        assertThrows(TheatreNotFoundException.class, () -> service.updateTheatre(2L, new UpdateTheatreRequest()));
        verify(theatreRepository, never()).save(any());
    }

    @Test void updateTheatre_whenCityInactive_throwsInactiveCityException() {
        city.setStatus(CityStatus.INACTIVE);
        when(theatreRepository.findById(1L)).thenReturn(Optional.of(theatre));
        assertThrows(InactiveCityException.class, () -> service.updateTheatre(1L, new UpdateTheatreRequest()));
        verify(theatreRepository, never()).save(any());
    }

    @Test void updateTheatreStatus_changesStatusAndSaves() {
        TheatreStatusUpdateRequest req = new TheatreStatusUpdateRequest(); req.setStatus(TheatreStatus.INACTIVE);
        when(theatreRepository.findById(1L)).thenReturn(Optional.of(theatre));
        when(theatreRepository.save(theatre)).thenReturn(theatre);
        when(mapper.toResponse(theatre)).thenReturn(response);
        assertSame(response, service.updateTheatreStatus(1L, req));
        assertEquals(TheatreStatus.INACTIVE, theatre.getStatus());
        verify(theatreRepository).save(theatre);
    }
}
