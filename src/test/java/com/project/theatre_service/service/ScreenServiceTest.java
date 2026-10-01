package com.project.theatre_service.service;

import com.project.theatre_service.dto.screen.*;
import com.project.theatre_service.entity.screen.*;
import com.project.theatre_service.entity.theatre.Theatre;
import com.project.theatre_service.mapper.ScreenMapper;
import com.project.theatre_service.exception.ScreenNotFoundException;
import com.project.theatre_service.exception.TheatreNotFoundException;
import com.project.theatre_service.repository.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ScreenServiceTest {
    @Mock ScreenRepository screenRepository;
    @Mock TheatreRepository theatreRepository;
    @Mock ScreenMapper mapper;
    @InjectMocks ScreenService service;
    Theatre theatre; Screen screen; ScreenResponse response;

    @BeforeEach void setUp() {
        theatre = new Theatre(); theatre.setId(3L);
        screen = new Screen(); screen.setId(4L); screen.setTheatre(theatre);
        screen.setScreenNumber(1); screen.setName("Screen 1"); screen.setScreenType(ScreenType.STANDARD);
        screen.setStatus(ScreenStatus.ACTIVE);
        response = new ScreenResponse(); response.setId(4L);
    }

    @Test void createScreen_savesActiveScreenWithParentAndTimestamps() {
        CreateScreenRequest req = new CreateScreenRequest(); req.setScreenNumber(1); req.setScreenType(ScreenType.STANDARD);
        when(theatreRepository.findById(3L)).thenReturn(Optional.of(theatre));
        when(mapper.toEntity(req, theatre)).thenReturn(screen);
        when(screenRepository.save(screen)).thenReturn(screen);
        when(mapper.toResponse(screen)).thenReturn(response);
        assertSame(response, service.createScreen(3L, req));
        assertEquals(ScreenStatus.ACTIVE, screen.getStatus());
        assertNotNull(screen.getCreatedAt()); assertNotNull(screen.getUpdatedAt());
        verify(screenRepository).save(screen);
    }

    @Test void createScreen_whenTheatreMissing_throwsTheatreNotFoundException() {
        when(theatreRepository.findById(3L)).thenReturn(Optional.empty());
        assertThrows(TheatreNotFoundException.class, () -> service.createScreen(3L, new CreateScreenRequest()));
        verifyNoInteractions(screenRepository);
    }

    @Test void getScreen_returnsMappedResponse() {
        when(screenRepository.findById(4L)).thenReturn(Optional.of(screen));
        when(mapper.toResponse(screen)).thenReturn(response);
        assertSame(response, service.getScreen(4L));
    }

    @Test void getScreen_whenMissing_throwsScreenNotFoundException() {
        when(screenRepository.findById(44L)).thenReturn(Optional.empty());
        assertThrows(ScreenNotFoundException.class, () -> service.getScreen(44L));
    }

    @Test void getScreenByTheatres_mapsResults() {
        when(screenRepository.findByTheatre_Id(3L)).thenReturn(List.of(screen));
        when(mapper.toResponse(screen)).thenReturn(response);
        assertEquals(List.of(response), service.getScreenByTheatres(3L));
        verify(screenRepository).findByTheatre_Id(3L);
    }

    @Test void getScreenByTheatres_whenEmpty_returnsEmptyList() {
        when(screenRepository.findByTheatre_Id(3L)).thenReturn(List.of());
        assertTrue(service.getScreenByTheatres(3L).isEmpty());
        verifyNoInteractions(mapper);
    }

    @Test void updateScreen_updatesFieldsAndTimestamp() {
        UpdateScreenRequest req = new UpdateScreenRequest();
        when(screenRepository.findById(4L)).thenReturn(Optional.of(screen));
        when(screenRepository.save(screen)).thenReturn(screen);
        when(mapper.toResponse(screen)).thenReturn(response);
        assertSame(response, service.updateScreen(4L, req));
        verify(mapper).updateEntity(req, screen);
        verify(screenRepository).save(screen);
        assertNotNull(screen.getUpdatedAt());
    }

    @Test void updateScreen_whenMissing_throwsScreenNotFoundException() {
        when(screenRepository.findById(9L)).thenReturn(Optional.empty());
        assertThrows(ScreenNotFoundException.class, () -> service.updateScreen(9L, new UpdateScreenRequest()));
        verify(screenRepository, never()).save(any());
    }

    @Test void updateScreenStatus_changesStatusAndSaves() {
        ScreenStatusUpdateRequest req = new ScreenStatusUpdateRequest(); req.setStatus(ScreenStatus.INACTIVE);
        when(screenRepository.findById(4L)).thenReturn(Optional.of(screen));
        when(screenRepository.save(screen)).thenReturn(screen);
        when(mapper.toResponse(screen)).thenReturn(response);
        assertSame(response, service.updateScreenStatus(4L, req));
        assertEquals(ScreenStatus.INACTIVE, screen.getStatus());
        assertNotNull(screen.getUpdatedAt());
        verify(screenRepository).save(screen);
    }

    @Test void updateScreenStatus_whenMissing_throwsScreenNotFoundException() {
        when(screenRepository.findById(9L)).thenReturn(Optional.empty());
        assertThrows(ScreenNotFoundException.class, () -> service.updateScreenStatus(9L, new ScreenStatusUpdateRequest()));
        verify(screenRepository, never()).save(any());
    }
}
