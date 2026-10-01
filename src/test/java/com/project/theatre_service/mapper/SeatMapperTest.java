package com.project.theatre_service.mapper;
import com.project.theatre_service.dto.seat.*;
import com.project.theatre_service.entity.screen.Screen;
import com.project.theatre_service.entity.seat.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
class SeatMapperTest {
 private final SeatMapper mapper=new SeatMapper();
 @Test void toEntity_mapsRequestAndScreen(){var r=new CreateSeatRequest();r.setRowLabel("A");r.setSeatNumber(12);r.setSeatType(SeatType.PREMIUM);Screen screen=new Screen();screen.setId(4L);Seat s=mapper.toEntity(r,screen);assertEquals("A",s.getRowLabel());assertEquals(12,s.getSeatNumber());assertEquals(SeatType.PREMIUM,s.getSeatType());assertSame(screen,s.getScreen());}
 @Test void updateSeatEntity_updatesEditableFields(){var r=new UpdateSeatRequest();r.setRowLabel("B");r.setSeatNumber(5);r.setSeatType(SeatType.RECLINER);Seat s=new Seat();mapper.updateSeatEntity(r,s);assertEquals("B",s.getRowLabel());assertEquals(5,s.getSeatNumber());assertEquals(SeatType.RECLINER,s.getSeatType());}
 @Test void toResponse_mapsSeatAndDerivedLabel(){Screen screen=new Screen();screen.setId(4L);Seat s=new Seat();s.setId(9L);s.setScreen(screen);s.setRowLabel("A");s.setSeatNumber(12);s.setSeatType(SeatType.PREMIUM);s.setStatus(SeatStatus.ACTIVE);var now=java.time.LocalDateTime.now();s.setCreatedAt(now);s.setUpdatedAt(now);var r=mapper.toResponse(s);assertEquals(9L,r.getId());assertEquals(4L,r.getScreenId());assertEquals("A",r.getRowLabel());assertEquals(12,r.getSeatNumber());assertEquals("A12",r.getSeatLabel());assertEquals("PREMIUM",r.getSeatType());assertEquals("ACTIVE",r.getStatus());assertEquals(now,r.getCreatedAt());assertEquals(now,r.getUpdatedAt());}
}