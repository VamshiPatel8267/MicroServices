package com.project.movie_service.movieController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.movie_service.dto.CreateMovieRequest;
import com.project.movie_service.dto.MovieResponse;
import com.project.movie_service.dto.MovieStatusUpdateRequest;
import com.project.movie_service.dto.UpdateMovieRequest;
import com.project.movie_service.entity.MovieStatus;
import com.project.movie_service.exception.ErrorResponse;
import com.project.movie_service.exception.GlobalExceptionHandler;
import com.project.movie_service.exception.InvalidPaginationException;
import com.project.movie_service.exception.MovieNotFoundException;
import com.project.movie_service.services.MovieService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class MovieControllerTest {

    @Mock
    private MovieService movieService;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();

        PageableHandlerMethodArgumentResolver pageableResolver =
                new PageableHandlerMethodArgumentResolver();

        mockMvc = MockMvcBuilders
                .standaloneSetup(new MovieController(movieService))
                .setControllerAdvice(new GlobalExceptionHandler())
                .setCustomArgumentResolvers(pageableResolver)
                .build();
    }

    @Test
    void createMovie_validRequest_shouldReturn201() throws Exception {
        CreateMovieRequest request = new CreateMovieRequest();
        request.setTitle("Interstellar");
        request.setDurationMinutes(169);

        MovieResponse response = movieResponse(1L, "Interstellar", MovieStatus.ACTIVE);
        when(movieService.createMovie(any(CreateMovieRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/v1/movies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Interstellar"))
                .andExpect(jsonPath("$.status").value("ACTIVE"));

        verify(movieService).createMovie(any(CreateMovieRequest.class));
    }

    @Test
    void createMovie_blankTitle_shouldReturn400WithFieldErrors() throws Exception {
        String body = """
                {
                  "title": "",
                  "durationMinutes": 169
                }
                """;

        mockMvc.perform(post("/api/v1/movies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value(org.hamcrest.Matchers.containsString("title")));

        verifyNoInteractions(movieService);
    }

    @Test
    void createMovie_negativeDuration_shouldReturn400() throws Exception {
        String body = """
                {
                  "title": "Interstellar",
                  "durationMinutes": -10
                }
                """;

        mockMvc.perform(post("/api/v1/movies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));

        verifyNoInteractions(movieService);
    }

    @Test
    void createMovie_missingDuration_shouldReturn400() throws Exception {
        String body = """
                {
                  "title": "Interstellar"
                }
                """;

        mockMvc.perform(post("/api/v1/movies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));

        verifyNoInteractions(movieService);
    }

    @Test
    void createMovie_titleOver200Characters_shouldReturn400() throws Exception {
        String longTitle = "A".repeat(201);
        String body = objectMapper.createObjectNode()
                .put("title", longTitle)
                .put("durationMinutes", 169)
                .toString();

        mockMvc.perform(post("/api/v1/movies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));

        verifyNoInteractions(movieService);
    }

    @Test
    void createMovie_malformedJson_shouldReturn400() throws Exception {
        mockMvc.perform(post("/api/v1/movies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"Interstellar\",\"durationMinutes\":"))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(movieService);
    }

    @Test
    void createMovie_wrongDurationType_shouldReturn400() throws Exception {
        String body = """
                {
                  "title": "Interstellar",
                  "durationMinutes": "abc"
                }
                """;

        mockMvc.perform(post("/api/v1/movies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(movieService);
    }

    @Test
    void getMovie_existing_shouldReturn200() throws Exception {
        when(movieService.getMovie(1L)).thenReturn(movieResponse(1L, "Interstellar", MovieStatus.ACTIVE));

        mockMvc.perform(get("/api/v1/movies/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Interstellar"));
    }

    @Test
    void getMovie_missing_shouldReturn404() throws Exception {
        when(movieService.getMovie(999L))
                .thenThrow(new MovieNotFoundException("Movie with id 999 not found"));

        mockMvc.perform(get("/api/v1/movies/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message").value("Movie with id 999 not found"))
                .andExpect(jsonPath("$.path").value("/api/v1/movies/999"));
    }

    @Test
    void getMovies_withoutFilter_shouldReturn200() throws Exception {
        MovieResponse response = movieResponse(1L, "Interstellar", MovieStatus.ACTIVE);
        when(movieService.getMovies(isNull(), any())).thenReturn(
                new PageImpl<>(List.of(response), PageRequest.of(0, 20), 1));

        mockMvc.perform(get("/api/v1/movies?page=0&size=20"))
                .andDo(org.springframework.test.web.servlet.result.MockMvcResultHandlers.print())
                .andExpect(status().isOk());
    }

    @Test
    void getMovies_withStatus_shouldPassStatusToService() throws Exception {

        when(movieService.getMovies(eq(MovieStatus.ACTIVE), any()))
                .thenReturn(
                        new PageImpl<>(
                                List.of(
                                        movieResponse(
                                                1L,
                                                "Interstellar",
                                                MovieStatus.ACTIVE
                                        )
                                ),
                                PageRequest.of(0, 20),
                                1
                        )
                );

        mockMvc.perform(get("/api/v1/movies?status=ACTIVE&page=0&size=20"))
                .andExpect(status().isOk());

        verify(movieService).getMovies(eq(MovieStatus.ACTIVE), any());
    }

    @Test
    void getMovies_size99_shouldBeAllowed() throws Exception {
        when(movieService.getMovies(isNull(), any()))
                .thenReturn(new PageImpl<>(List.of(), PageRequest.of(0, 99), 0));

        mockMvc.perform(get("/api/v1/movies?size=99"))
                .andExpect(status().isOk());
    }

    @Test
    void getMovies_size100_shouldBeAllowed_whenMaximumIs100() throws Exception {
        when(movieService.getMovies(isNull(), any()))
                .thenReturn(new PageImpl<>(List.of(), PageRequest.of(0, 100), 0));

        // This is a boundary test. The current branch uses >= 100 and is expected to fail.
        mockMvc.perform(get("/api/v1/movies?size=100"))
                .andExpect(status().isOk());
    }

    @Test
    void getMovies_size101_shouldReturn400() throws Exception {
        mockMvc.perform(get("/api/v1/movies?size=101"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("BAD_REQUEST"));

        verifyNoInteractions(movieService);
    }

    @Test
    void getMovies_largePageNumber_shouldNotBeRejectedBySizeGuard() throws Exception {
        when(movieService.getMovies(isNull(), any()))
                .thenReturn(new PageImpl<>(List.of(), PageRequest.of(1_000_000_000, 20), 0));

        mockMvc.perform(get("/api/v1/movies?page=1000000000&size=20"))
                .andExpect(status().isOk());

        verify(movieService).getMovies(isNull(), any());
    }

    @Test
    void updateMovie_validRequest_shouldReturn200() throws Exception {
        UpdateMovieRequest request = new UpdateMovieRequest();
        request.setTitle("Interstellar Updated");
        request.setDurationMinutes(170);

        when(movieService.updateMovie(eq(1L), any(UpdateMovieRequest.class)))
                .thenReturn(movieResponse(1L, "Interstellar Updated", MovieStatus.ACTIVE));

        mockMvc.perform(put("/api/v1/movies/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Interstellar Updated"));
    }

    @Test
    void updateMovie_invalidRequest_shouldReturn400() throws Exception {
        String body = """
                {
                  "title": "",
                  "durationMinutes": -1
                }
                """;

        mockMvc.perform(put("/api/v1/movies/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(movieService);
    }

    @Test
    void updateMovie_missing_shouldReturn404() throws Exception {
        String body = """
                {
                  "title": "Updated",
                  "durationMinutes": 170
                }
                """;
        when(movieService.updateMovie(eq(999L), any(UpdateMovieRequest.class)))
                .thenThrow(new MovieNotFoundException("Movie with Id 999 NOT Found"));

        mockMvc.perform(put("/api/v1/movies/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateMovieStatus_validRequest_shouldReturn200() throws Exception {
        MovieStatusUpdateRequest request = new MovieStatusUpdateRequest();
        request.setStatus(MovieStatus.INACTIVE);
        when(movieService.updateMovieStatus(1L, MovieStatus.INACTIVE))
                .thenReturn(movieResponse(1L, "Interstellar", MovieStatus.INACTIVE));

        mockMvc.perform(patch("/api/v1/movies/1/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("INACTIVE"));
    }

    @Test
    void updateMovieStatus_missingStatus_shouldReturn400() throws Exception {
        mockMvc.perform(patch("/api/v1/movies/1/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(movieService);
    }

    @Test
    void updateMovieStatus_missingMovie_shouldReturn404() throws Exception {
        when(movieService.updateMovieStatus(999L, MovieStatus.INACTIVE))
                .thenThrow(new MovieNotFoundException("Movie with Id 999 NOT Found"));

        mockMvc.perform(patch("/api/v1/movies/999/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"status\":\"INACTIVE\"}"))
                .andExpect(status().isNotFound());
    }

    private MovieResponse movieResponse(Long id, String title, MovieStatus status) {
        MovieResponse response = new MovieResponse();
        response.setId(id);
        response.setTitle(title);
        response.setDurationMinutes(169);
        response.setStatus(status.name());
        return response;
    }
}
