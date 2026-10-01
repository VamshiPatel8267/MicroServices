package com.project.theatre_service.controller;

import com.project.theatre_service.exception.GlobalExceptionHandler;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import com.project.theatre_service.dto.screen.*;
import com.project.theatre_service.service.ScreenService;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class ScreenControllerTest {
 private ScreenService service=mock(ScreenService.class); private MockMvc mvc;
 @BeforeEach void setup(){var v=new LocalValidatorFactoryBean();v.afterPropertiesSet();mvc=MockMvcBuilders.standaloneSetup(new ScreenController(service)).setControllerAdvice(new GlobalExceptionHandler()).setValidator(v).build();}
 @Test void createReturns201() throws Exception {when(service.createScreen(eq(1L),any())).thenReturn(new ScreenResponse());mvc.perform(post("/api/v1/screen/1").contentType("application/json").content("{\"screenNumber\":1,\"name\":\"Screen 1\",\"screenType\":\"STANDARD\"}")).andExpect(status().isCreated());}
 @Test void createRejectsNonPositiveNumber() throws Exception {mvc.perform(post("/api/v1/screen/1").contentType("application/json").content("{\"screenNumber\":0,\"screenType\":\"STANDARD\"}")).andExpect(status().isBadRequest());verifyNoInteractions(service);}
 @Test void byTheatreReturns200() throws Exception {when(service.getScreenByTheatres(1L)).thenReturn(List.of());mvc.perform(get("/api/v1/screen/theatre/1")).andExpect(status().isOk()).andExpect(jsonPath("$").isArray());}
 @Test void getReturns200() throws Exception {when(service.getScreen(1L)).thenReturn(new ScreenResponse());mvc.perform(get("/api/v1/screen/1")).andExpect(status().isOk());}
 @Test void updateReturns200() throws Exception {when(service.updateScreen(eq(1L),any())).thenReturn(new ScreenResponse());mvc.perform(put("/api/v1/screen/1").contentType("application/json").content("{\"screenNumber\":2,\"screenType\":\"IMAX\"}")).andExpect(status().isOk());}
 @Test void statusReturns200() throws Exception {when(service.updateScreenStatus(eq(1L),any())).thenReturn(new ScreenResponse());mvc.perform(patch("/api/v1/screen/1/status").contentType("application/json").content("{\"status\":\"ACTIVE\"}")).andExpect(status().isOk());}
}