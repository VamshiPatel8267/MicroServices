package com.project.theatre_service.controller;

import com.project.theatre_service.exception.GlobalExceptionHandler;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import com.project.theatre_service.dto.seat.*;
import com.project.theatre_service.service.SeatService;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class SeatControllerTest {
 private SeatService service=mock(SeatService.class); private MockMvc mvc;
 @BeforeEach void setup(){var v=new LocalValidatorFactoryBean();v.afterPropertiesSet();mvc=MockMvcBuilders.standaloneSetup(new SeatController(service)).setControllerAdvice(new GlobalExceptionHandler()).setValidator(v).build();}
 @Test void createReturns200AsDefinedByController() throws Exception {when(service.createSeat(eq(1L),any())).thenReturn(new SeatResponse());mvc.perform(post("/api/v1/seats/1").contentType("application/json").content("{\"rowLabel\":\"A\",\"seatNumber\":1,\"seatType\":\"REGULAR\"}")).andExpect(status().isOk());}
 @Test void createRejectsInvalidSeatNumber() throws Exception {mvc.perform(post("/api/v1/seats/1").contentType("application/json").content("{\"rowLabel\":\"A\",\"seatNumber\":0,\"seatType\":\"REGULAR\"}")).andExpect(status().isBadRequest());verifyNoInteractions(service);}
 @Test void byScreenReturns200() throws Exception {when(service.getSeatsByScreen(1L)).thenReturn(List.of());mvc.perform(get("/api/v1/seats/screen/1")).andExpect(status().isOk()).andExpect(jsonPath("$").isArray());}
 @Test void getReturns200() throws Exception {when(service.getSeat(1L)).thenReturn(new SeatResponse());mvc.perform(get("/api/v1/seats/1")).andExpect(status().isOk());}
 @Test void updateReturns200() throws Exception {when(service.updateSeat(eq(1L),any())).thenReturn(new SeatResponse());mvc.perform(put("/api/v1/seats/1").contentType("application/json").content("{\"rowLabel\":\"B\",\"seatNumber\":2,\"seatType\":\"PREMIUM\"}")).andExpect(status().isOk());}
 @Test void statusReturns200() throws Exception {when(service.updateSeatStatus(eq(1L),any())).thenReturn(new SeatResponse());mvc.perform(patch("/api/v1/seats/1/status").contentType("application/json").content("{\"status\":\"ACTIVE\"}")).andExpect(status().isOk());}
}