package com.project.theatre_service.mapper;
import com.project.theatre_service.dto.screen.*;
import com.project.theatre_service.entity.screen.*;
import com.project.theatre_service.entity.theatre.Theatre;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
class ScreenMapperTest {
 private final ScreenMapper mapper=new ScreenMapper();
 @Test void toEntity_mapsRequestAndTheatre(){var r=new CreateScreenRequest();r.setScreenNumber(2);r.setName("Gold");r.setScreenType(ScreenType.IMAX);Theatre t=new Theatre();t.setId(7L);Screen s=mapper.toEntity(r,t);assertEquals(2,s.getScreenNumber());assertEquals("Gold",s.getName());assertEquals(ScreenType.IMAX,s.getScreenType());assertSame(t,s.getTheatre());}
 @Test void updateEntity_changesEditableFields(){var r=new UpdateScreenRequest();r.setScreenNumber(3);r.setName("Premium");r.setScreenType(ScreenType.DOLBY_ATMOS);Screen s=new Screen();mapper.updateEntity(r,s);assertEquals(3,s.getScreenNumber());assertEquals("Premium",s.getName());assertEquals(ScreenType.DOLBY_ATMOS,s.getScreenType());}
 @Test void toResponse_mapsScreenAndParentId(){Theatre t=new Theatre();t.setId(7L);Screen s=new Screen();s.setId(8L);s.setTheatre(t);s.setScreenNumber(2);s.setName("Gold");s.setScreenType(ScreenType.IMAX);s.setStatus(ScreenStatus.ACTIVE);var now=java.time.LocalDateTime.now();s.setCreatedAt(now);s.setUpdatedAt(now);var r=mapper.toResponse(s);assertEquals(8L,r.getId());assertEquals(7L,r.getTheatreId());assertEquals(2,r.getScreenNumber());assertEquals("Gold",r.getName());assertEquals("IMAX",r.getScreenType());assertEquals("ACTIVE",r.getStatus());assertEquals(now,r.getCreatedAt());assertEquals(now,r.getUpdatedAt());}
}