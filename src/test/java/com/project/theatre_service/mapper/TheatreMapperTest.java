package com.project.theatre_service.mapper;
import com.project.theatre_service.dto.theatre.*;
import com.project.theatre_service.entity.city.*;
import com.project.theatre_service.entity.theatre.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
class TheatreMapperTest {
 private final TheatreMapper mapper=new TheatreMapper();
 @Test void toEntity_mapsRequestAndCity(){var r=new CreateTheatreRequest();r.setName("PVR");r.setAddress("Madhapur");City c=new City();c.setId(2L);Theatre t=mapper.toEntity(r,c);assertEquals("PVR",t.getName());assertEquals("Madhapur",t.getAddress());assertSame(c,t.getCity());}
 @Test void updateEntity_updatesFieldsAndCity(){var r=new UpdateTheatreRequest();r.setName("INOX");r.setAddress("Hitech City");City c=new City();c.setId(3L);Theatre t=new Theatre();mapper.updateEntity(r,t,c);assertEquals("INOX",t.getName());assertEquals("Hitech City",t.getAddress());assertSame(c,t.getCity());}
 @Test void toResponse_mapsTheatreAndCityDetails(){City c=new City();c.setId(2L);c.setName("Hyderabad");c.setState("Telangana");c.setCountry("India");Theatre t=new Theatre();t.setId(5L);t.setName("PVR");t.setAddress("Madhapur");t.setCity(c);t.setStatus(TheatreStatus.ACTIVE);var now=java.time.LocalDateTime.now();t.setCreatedAt(now);t.setUpdatedAt(now);var r=mapper.toResponse(t);assertEquals(5L,r.getId());assertEquals("PVR",r.getName());assertEquals("Madhapur",r.getAddress());assertEquals(2L,r.getCityId());assertEquals("Hyderabad",r.getCityName());assertEquals("Telangana",r.getState());assertEquals("India",r.getCountry());assertEquals("ACTIVE",r.getStatus());assertEquals(now,r.getCreatedAt());assertEquals(now,r.getUpdatedAt());}
}