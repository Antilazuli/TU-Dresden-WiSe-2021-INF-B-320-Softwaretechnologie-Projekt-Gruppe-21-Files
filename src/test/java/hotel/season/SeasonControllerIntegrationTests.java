package hotel.season;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;

import hotel.AbstractIntegrationTests;

class SeasonControllerIntegrationTests extends AbstractIntegrationTests {

    private SeasonRepository repository;

    private SeasonController controller;

    // Für alle Tests außer die beiden Startests: 'test...'
    // Die anderen Tests funktionieren sonst nicht, liegt an 'validateDates' in
    // 'SeasonController' Line 76
    @Autowired
    SeasonController controller2;

    @BeforeEach
    public void setup() {

        repository = mock(SeasonRepository.class);

        controller = new SeasonController(repository);
    }

    @Test
    void testSeasonsView() {

        var model = new ExtendedModelMap();

        assertEquals("manager/seasons", controller.seasons(model));

        assertEquals(repository.findAll(), model.asMap().get("seasons"));
    }

    @Test
    void testAddSeasonView() {

        Model model = new ExtendedModelMap();

        assertEquals("manager/addSeason", controller.addSeason(model, mock(SeasonForm.class)));
    }

    @Test
    @WithMockUser(roles = "MANAGER")
    void addSeason() {

        String returnedView = controller2.addSeason(new SeasonForm("test", "100", "2022-10-20", "2022-10-25"),
                mock(Errors.class));

        assertEquals("redirect:/manager/seasons", returnedView);
    }

    @Test
    @WithMockUser(roles = "MANAGER")
    void addSeasonStartError() {

        Errors errors = mock(Errors.class);
        when(errors.hasFieldErrors("start")).thenReturn(true);

        String returnedView = controller2.addSeason(new SeasonForm("test", "100", "test", "2022-10-25"), errors);

        assertEquals("manager/addSeason", returnedView);
    }

    @Test
    @WithMockUser(roles = "MANAGER")
    void addSeasonEndError() {

        Errors errors = mock(Errors.class);
        when(errors.hasFieldErrors("end")).thenReturn(true);

        String returnedView = controller2.addSeason(new SeasonForm("test", "100", "2022-10-20", "test"), errors);

        assertEquals("manager/addSeason", returnedView);
    }

    @Test
    @WithMockUser(roles = "MANAGER")
    void addSeasonErrors() {

        Errors errors = mock(Errors.class);
        when(errors.hasErrors()).thenReturn(true);

        String returnedView = controller2.addSeason(new SeasonForm("test", "100", "2022-10-20", "2022-10-25"), errors);

        assertEquals("manager/addSeason", returnedView);
    }

    @Test
    @WithMockUser(roles = "MANAGER")
    void removeSeason() {

        controller2.addSeason(new SeasonForm("test", "100", "2022-10-20", "2022-10-25"), mock(Errors.class));

        String returnedView = controller2
                .remove(new Season("test", 100, LocalDate.of(2022, 1, 23), LocalDate.of(2022, 1, 25)));

        assertEquals("redirect:/manager/seasons", returnedView);
    }
}
