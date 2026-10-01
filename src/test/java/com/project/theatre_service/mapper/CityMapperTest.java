package com.project.theatre_service.mapper;
import com.project.theatre_service.dto.city.*;
import com.project.theatre_service.entity.city.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
class CityMapperTest {
 private final CityMapper mapper=new CityMapper();
 @Test void toEntity_mapsRequestFields(){CreateCityRequest r=new CreateCityRequest();r.setName("Hyderabad");r.setState("Telangana");r.setCountry("India");City c=mapper.toEntity(r);assertEquals("Hyderabad",c.getName());assertEquals("Telangana",c.getState());assertEquals("India",c.getCountry());}
 @Test void toResponse_mapsAllFields(){City c=new City();c.setId(1L);c.setName("Hyderabad");c.setState("Telangana");c.setCountry("India");c.setStatus(CityStatus.ACTIVE);var now=java.time.LocalDateTime.now();c.setCreatedAt(now);c.setUpdatedAt(now);CityResponse r=mapper.toResponse(c);assertEquals(1L,r.getId());assertEquals("Hyderabad",r.getName());assertEquals("Telangana",r.getState());assertEquals("India",r.getCountry());assertEquals("ACTIVE",r.getStatus());assertEquals(now,r.getCreatedAt());assertEquals(now,r.getUpdatedAt());}
}