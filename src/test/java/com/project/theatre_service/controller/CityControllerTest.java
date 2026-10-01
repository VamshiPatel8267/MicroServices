package com.project.theatre_service.controller;

import com.project.theatre_service.exception.GlobalExceptionHandler;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import com.project.theatre_service.dto.city.*;
import com.project.theatre_service.service.CityService;
import com.project.theatre_service.entity.city.CityStatus;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class CityControllerTest {
 private CityService service=mock(CityService.class); private MockMvc mvc;
 @BeforeEach void setup(){var validator=new LocalValidatorFactoryBean();validator.afterPropertiesSet();mvc=MockMvcBuilders.standaloneSetup(new CityController(service)).setControllerAdvice(new GlobalExceptionHandler()).setValidator(validator).build();}
 @Test void createReturns201() throws Exception {when(service.createCity(any())).thenReturn(new CityResponse());mvc.perform(post("/api/v1/cities").contentType("application/json").content("{\"name\":\"Hyderabad\",\"state\":\"Telangana\",\"country\":\"India\"}")).andExpect(status().isCreated());verify(service).createCity(any());}
 @Test void createRejectsBlankName() throws Exception {mvc.perform(post("/api/v1/cities").contentType("application/json").content("{\"name\":\" \",\"state\":\"Telangana\",\"country\":\"India\"}")).andExpect(status().isBadRequest());verifyNoInteractions(service);}
 @Test void listReturns200() throws Exception {when(service.getAllCities()).thenReturn(List.of(new CityResponse()));mvc.perform(get("/api/v1/cities")).andExpect(status().isOk()).andExpect(jsonPath("$").isArray());}
 @Test void getReturns200() throws Exception {when(service.getCity(1L)).thenReturn(new CityResponse());mvc.perform(get("/api/v1/cities/1")).andExpect(status().isOk());}
 @Test void statusReturns202() throws Exception {when(service.updateCityStatus(eq(1L),any())).thenReturn(new CityResponse());mvc.perform(patch("/api/v1/cities/1/status").contentType("application/json").content("{\"status\":\"ACTIVE\"}")).andExpect(status().isAccepted());}
}