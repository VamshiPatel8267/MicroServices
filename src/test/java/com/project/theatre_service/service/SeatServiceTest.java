package com.project.theatre_service.service;

import com.project.theatre_service.dto.seat.*;
import com.project.theatre_service.entity.screen.Screen;
import com.project.theatre_service.entity.seat.*;
import com.project.theatre_service.mapper.SeatMapper;
import com.project.theatre_service.exception.SeatNotFoundException;
import com.project.theatre_service.exception.ScreenNotFoundException;
import com.project.theatre_service.repository.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SeatServiceTest {
    @Mock ScreenRepository screenRepository;
    @Mock SeatRepository seatRepository;
    @Mock SeatMapper mapper;
    @InjectMocks SeatService service;
    Screen screen; Seat seat; SeatResponse response;

    @BeforeEach void setUp() {
        screen = new Screen(); screen.setId(3L);
        seat = new Seat(); seat.setId(8L); seat.setScreen(screen);
        seat.setRowLabel("A"); seat.setSeatNumber(1); seat.setSeatType(SeatType.REGULAR);
        seat.setStatus(SeatStatus.ACTIVE);
        response = new SeatResponse(); response.setId(8L); response.setSeatLabel("A1");
    }

    @Test void createSeat_savesActiveSeatWithParentAndTimestamps() {
        CreateSeatRequest req = new CreateSeatRequest(); req.setRowLabel("A"); req.setSeatNumber(1); req.setSeatType(SeatType.REGULAR);
        when(screenRepository.findById(3L)).thenReturn(Optional.of(screen));
        when(mapper.toEntity(req, screen)).thenReturn(seat);
        when(seatRepository.save(seat)).thenReturn(seat);
        when(mapper.toResponse(seat)).thenReturn(response);
        assertSame(response, service.createSeat(3L, req));
        assertEquals(SeatStatus.ACTIVE, seat.getStatus());
        assertNotNull(seat.getCreatedAt()); assertNotNull(seat.getUpdatedAt());
        verify(seatRepository).save(seat);
    }

    @Test void createSeat_whenScreenMissing_throwsScreenNotFoundException() {
        when(screenRepository.findById(3L)).thenReturn(Optional.empty());
        assertThrows(ScreenNotFoundException.class, () -> service.createSeat(3L, new CreateSeatRequest()));
        verifyNoInteractions(seatRepository);
    }

    @Test void getSeat_returnsMappedResponse() {
        when(seatRepository.findById(8L)).thenReturn(Optional.of(seat));
        when(mapper.toResponse(seat)).thenReturn(response);
        assertSame(response, service.getSeat(8L));
    }

    @Test void getSeat_whenMissing_throwsSeatNotFoundException() {
        when(seatRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(SeatNotFoundException.class, () -> service.getSeat(99L));
    }

    @Test void getSeatsByScreen_mapsResults() {
        when(seatRepository.findByScreen_Id(3L)).thenReturn(List.of(seat));
        when(mapper.toResponse(seat)).thenReturn(response);
        assertEquals(List.of(response), service.getSeatsByScreen(3L));
        verify(seatRepository).findByScreen_Id(3L);
    }

    @Test void getSeatsByScreen_whenEmpty_returnsEmptyList() {
        when(seatRepository.findByScreen_Id(3L)).thenReturn(List.of());
        assertTrue(service.getSeatsByScreen(3L).isEmpty());
        verifyNoInteractions(mapper);
    }

    @Test void updateSeat_updatesFieldsAndTimestamp() {
        UpdateSeatRequest req = new UpdateSeatRequest();
        when(seatRepository.findById(8L)).thenReturn(Optional.of(seat));
        when(seatRepository.save(seat)).thenReturn(seat);
        when(mapper.toResponse(seat)).thenReturn(response);
        assertSame(response, service.updateSeat(8L, req));
        verify(mapper).updateSeatEntity(req, seat);
        verify(seatRepository).save(seat);
        assertNotNull(seat.getUpdatedAt());
    }

    @Test void updateSeat_whenMissing_throwsSeatNotFoundException() {
        when(seatRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(SeatNotFoundException.class, () -> service.updateSeat(99L, new UpdateSeatRequest()));
        verify(seatRepository, never()).save(any());
    }

    @Test void updateSeatStatus_changesStatusAndSaves() {
        SeatStatusUpdateRequest req = new SeatStatusUpdateRequest(); req.setStatus(SeatStatus.INACTIVE);
        when(seatRepository.findById(8L)).thenReturn(Optional.of(seat));
        when(seatRepository.save(seat)).thenReturn(seat);
        when(mapper.toResponse(seat)).thenReturn(response);
        assertSame(response, service.updateSeatStatus(8L, req));
        assertEquals(SeatStatus.INACTIVE, seat.getStatus());
        assertNotNull(seat.getUpdatedAt());
        verify(seatRepository).save(seat);
    }

    @Test void updateSeatStatus_whenMissing_throwsSeatNotFoundException() {
        when(seatRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(SeatNotFoundException.class, () -> service.updateSeatStatus(99L, new SeatStatusUpdateRequest()));
        verify(seatRepository, never()).save(any());
    }
}
