package com.project.theatre_service.controller;

import com.project.theatre_service.exception.GlobalExceptionHandler;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import com.project.theatre_service.dto.theatre.*;
import com.project.theatre_service.service.TheatreService;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class TheatreControllerTest {
 private TheatreService service=mock(TheatreService.class); private MockMvc mvc;
 @BeforeEach void setup(){var v=new LocalValidatorFactoryBean();v.afterPropertiesSet();mvc=MockMvcBuilders.standaloneSetup(new TheatreController(service)).setControllerAdvice(new GlobalExceptionHandler()).setValidator(v).build();}
 @Test void createReturns201() throws Exception {when(service.createTheatre(any())).thenReturn(new TheatreResponse());mvc.perform(post("/api/v1/theatres").contentType("application/json").content("{\"name\":\"PVR\",\"address\":\"Road 1\",\"cityId\":1}")).andExpect(status().isCreated());}
 @Test void createRejectsInvalidCityId() throws Exception {mvc.perform(post("/api/v1/theatres").contentType("application/json").content("{\"name\":\"PVR\",\"address\":\"Road 1\",\"cityId\":0}")).andExpect(status().isBadRequest());verifyNoInteractions(service);}
 @Test void listReturns200() throws Exception {when(service.getAllTheatres()).thenReturn(List.of());mvc.perform(get("/api/v1/theatres")).andExpect(status().isOk()).andExpect(jsonPath("$").isArray());}
 @Test void getReturns200() throws Exception {when(service.getTheatre(1L)).thenReturn(new TheatreResponse());mvc.perform(get("/api/v1/theatres/1")).andExpect(status().isOk());}
 @Test void byCityReturns200() throws Exception {when(service.getTheatresByCity(1L)).thenReturn(List.of());mvc.perform(get("/api/v1/theatres/cities/1")).andExpect(status().isOk());}
 @Test void updateReturns200() throws Exception {when(service.updateTheatre(eq(1L),any())).thenReturn(new TheatreResponse());mvc.perform(put("/api/v1/theatres/1").contentType("application/json").content("{\"name\":\"PVR\",\"address\":\"Road 1\",\"cityId\":1}")).andExpect(status().isOk());}
 @Test void statusReturns200() throws Exception {when(service.updateTheatreStatus(eq(1L),any())).thenReturn(new TheatreResponse());mvc.perform(patch("/api/v1/theatres/1/status").contentType("application/json").content("{\"status\":\"ACTIVE\"}")).andExpect(status().isOk());}
}