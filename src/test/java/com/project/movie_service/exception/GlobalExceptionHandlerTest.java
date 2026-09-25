package com.project.movie_service.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.springframework.core.MethodParameter;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;

import static org.junit.jupiter.api.Assertions.*;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void handleMovieNotFound_shouldReturn404AndStandardErrorResponse() {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/api/v1/movies/999");
        ResponseEntity<ErrorResponse> response = handler.handleMovieNotFound(
                new MovieNotFoundException("Movie with id 999 not found"), request);

        assertEquals(404, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(404, response.getBody().getStatus());
        assertEquals("Not Found", response.getBody().getError());
        assertEquals("Movie with id 999 not found", response.getBody().getMessage());
        assertEquals("/api/v1/movies/999", response.getBody().getPath());
    }

    @Test
    void handlePaginationError_shouldReturn400AndExceptionMessage() {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/api/v1/movies");
        ResponseEntity<ErrorResponse> response = handler.handlePaginationError(
                new InvalidPaginationException("Page size cannot exceed 100"), request);

        assertEquals(400, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(400, response.getBody().getStatus());
        assertEquals("BAD_REQUEST", response.getBody().getError());
        assertEquals("Page size cannot exceed 100", response.getBody().getMessage());
        assertEquals("/api/v1/movies", response.getBody().getPath());
    }

    @Test
    void handleValidationError_shouldReturnCombinedFieldMessages() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest("POST", "/api/v1/movies");

        BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(
                new Object(), "createMovieRequest");
        bindingResult.addError(new FieldError("createMovieRequest", "title", "must not be blank"));
        bindingResult.addError(new FieldError("createMovieRequest", "durationMinutes", "must be greater than 0"));

        MethodArgumentNotValidException exception = new MethodArgumentNotValidException(
                new MethodParameter(GlobalExceptionHandlerTest.class.getDeclaredMethod("dummy", Object.class), 0),
                bindingResult);

        ResponseEntity<ErrorResponse> response = handler.handleValidationException(exception, request);

        assertEquals(400, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(400, response.getBody().getStatus());
        assertEquals("BAD_REQUEST", response.getBody().getError());
        // This assertion intentionally exposes the current branch bug:
        // the handler constructs `message` but currently returns ex.getMessage().
        assertTrue(response.getBody().getMessage().contains("title:must not be blank"));
        assertTrue(response.getBody().getMessage().contains("durationMinutes:must be greater than 0"));
        assertEquals("/api/v1/movies", response.getBody().getPath());
    }

    @SuppressWarnings("unused")
    private static void dummy(Object ignored) {
    }
}
