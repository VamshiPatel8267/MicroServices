package com.project.theatre_service.integration;

import com.project.theatre_service.entity.city.City;
import com.project.theatre_service.entity.city.CityStatus;
import com.project.theatre_service.entity.screen.Screen;
import com.project.theatre_service.entity.screen.ScreenStatus;
import com.project.theatre_service.entity.screen.ScreenType;
import com.project.theatre_service.entity.seat.Seat;
import com.project.theatre_service.entity.seat.SeatStatus;
import com.project.theatre_service.entity.seat.SeatType;
import com.project.theatre_service.entity.theatre.Theatre;
import com.project.theatre_service.entity.theatre.TheatreStatus;
import com.project.theatre_service.repository.CityRepository;
import com.project.theatre_service.repository.ScreenRepository;
import com.project.theatre_service.repository.SeatRepository;
import com.project.theatre_service.repository.TheatreRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class TheatrePersistenceIntegrationTest {

    @Autowired private CityRepository cityRepository;
    @Autowired private TheatreRepository theatreRepository;
    @Autowired private ScreenRepository screenRepository;
    @Autowired private SeatRepository seatRepository;
    @Autowired private JdbcTemplate jdbcTemplate;

    @Test
    void city_canBePersistedAndRetrieved() {
        City saved = cityRepository.saveAndFlush(city("Hyderabad", "Telangana", "India"));

        Optional<City> found = cityRepository.findById(saved.getId());

        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Hyderabad");
        assertThat(found.get().getStatus()).isEqualTo(CityStatus.ACTIVE);
    }

    @Test
    void theatre_persistsCityForeignKeyAndCanBeQueriedByCity() {
        City city = cityRepository.saveAndFlush(city("Pune", "Maharashtra", "India"));
        Theatre theatre = theatre("Central Cinema", city);
        Theatre saved = theatreRepository.saveAndFlush(theatre);

        theatreRepository.flush();
        List<Theatre> theatres = theatreRepository.findByCity_Id(city.getId());
        Theatre reloaded = theatreRepository.findById(saved.getId()).orElseThrow();

        assertThat(theatres).extracting(Theatre::getId).contains(saved.getId());
        assertThat(reloaded.getCity().getId()).isEqualTo(city.getId());
    }

    @Test
    void screen_persistsTheatreForeignKeyAndCanBeQueriedByTheatre() {
        Theatre theatre = createTheatre("Screen Test City", "Screen Test Theatre");
        Screen saved = screenRepository.saveAndFlush(screen(theatre, 1));

        List<Screen> screens = screenRepository.findByTheatre_Id(theatre.getId());
        Screen reloaded = screenRepository.findById(saved.getId()).orElseThrow();

        assertThat(screens).extracting(Screen::getId).contains(saved.getId());
        assertThat(reloaded.getTheatre().getId()).isEqualTo(theatre.getId());
        assertThat(reloaded.getScreenType()).isEqualTo(ScreenType.STANDARD);
    }

    @Test
    void seat_persistsScreenForeignKeyAndCanBeQueriedByScreen() {
        Screen screen = createScreen("Seat Test City", "Seat Test Theatre", 1);
        Seat saved = seatRepository.saveAndFlush(seat(screen, "A", 1));

        List<Seat> seats = seatRepository.findByScreen_Id(screen.getId());
        Seat reloaded = seatRepository.findById(saved.getId()).orElseThrow();

        assertThat(seats).extracting(Seat::getId).contains(saved.getId());
        assertThat(reloaded.getScreen().getId()).isEqualTo(screen.getId());
        assertThat(reloaded.getSeatType()).isEqualTo(SeatType.REGULAR);
    }

    @Test
    void duplicateScreenNumberWithinSameTheatre_isRejectedByDatabase() {
        Theatre theatre = createTheatre("Unique Screen City", "Unique Screen Theatre");
        screenRepository.saveAndFlush(screen(theatre, 1));

        assertThatThrownBy(() -> screenRepository.saveAndFlush(screen(theatre, 1)))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void sameScreenNumberInDifferentTheatres_isAllowed() {
        Theatre first = createTheatre("Different Theatre City", "Theatre One");
        Theatre second = theatreRepository.saveAndFlush(theatre("Theatre Two", first.getCity()));

        screenRepository.saveAndFlush(screen(first, 1));
        Screen secondScreen = screenRepository.saveAndFlush(screen(second, 1));

        assertThat(secondScreen.getId()).isNotNull();
    }

    @Test
    void duplicateSeatPositionWithinSameScreen_isRejectedByDatabase() {
        Screen screen = createScreen("Unique Seat City", "Unique Seat Theatre", 1);
        seatRepository.saveAndFlush(seat(screen, "A", 1));

        assertThatThrownBy(() -> seatRepository.saveAndFlush(seat(screen, "A", 1)))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void sameSeatNumberInDifferentRows_isAllowed() {
        Screen screen = createScreen("Different Row City", "Different Row Theatre", 1);
        seatRepository.saveAndFlush(seat(screen, "A", 1));

        Seat rowBSeat = seatRepository.saveAndFlush(seat(screen, "B", 1));

        assertThat(rowBSeat.getId()).isNotNull();
    }

    @Test
    void deletingCityReferencedByTheatre_isRejected() {
        City city = cityRepository.saveAndFlush(city("Restricted Delete City", "State", "Country"));
        theatreRepository.saveAndFlush(theatre("Referenced Theatre", city));

        // Use direct SQL here to test the database foreign-key constraint itself.
        // Calling repository.delete() exercises Hibernate entity-state handling as well,
        // which can fail before the database gets a chance to enforce the FK.
        assertThatThrownBy(() -> jdbcTemplate.update(
                "DELETE FROM cities WHERE id = ?", city.getId()))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    private Theatre createTheatre(String cityName, String theatreName) {
        City city = cityRepository.saveAndFlush(city(cityName, "Test State", "Test Country"));
        return theatreRepository.saveAndFlush(theatre(theatreName, city));
    }

    private Screen createScreen(String cityName, String theatreName, int screenNumber) {
        Theatre theatre = createTheatre(cityName, theatreName);
        return screenRepository.saveAndFlush(screen(theatre, screenNumber));
    }

    private City city(String name, String state, String country) {
        LocalDateTime now = LocalDateTime.now();
        City city = new City();
        city.setName(name);
        city.setState(state);
        city.setCountry(country);
        city.setStatus(CityStatus.ACTIVE);
        city.setCreatedAt(now);
        city.setUpdatedAt(now);
        return city;
    }

    private Theatre theatre(String name, City city) {
        LocalDateTime now = LocalDateTime.now();
        Theatre theatre = new Theatre();
        theatre.setName(name);
        theatre.setAddress("123 Test Street");
        theatre.setCity(city);
        theatre.setStatus(TheatreStatus.ACTIVE);
        theatre.setCreatedAt(now);
        theatre.setUpdatedAt(now);
        return theatre;
    }

    private Screen screen(Theatre theatre, int number) {
        LocalDateTime now = LocalDateTime.now();
        Screen screen = new Screen();
        screen.setTheatre(theatre);
        screen.setScreenNumber(number);
        screen.setName("Screen " + number);
        screen.setScreenType(ScreenType.STANDARD);
        screen.setStatus(ScreenStatus.ACTIVE);
        screen.setCreatedAt(now);
        screen.setUpdatedAt(now);
        return screen;
    }

    private Seat seat(Screen screen, String row, int number) {
        LocalDateTime now = LocalDateTime.now();
        Seat seat = new Seat();
        seat.setScreen(screen);
        seat.setRowLabel(row);
        seat.setSeatNumber(number);
        seat.setSeatType(SeatType.REGULAR);
        seat.setStatus(SeatStatus.ACTIVE);
        seat.setCreatedAt(now);
        seat.setUpdatedAt(now);
        return seat;
    }
}
