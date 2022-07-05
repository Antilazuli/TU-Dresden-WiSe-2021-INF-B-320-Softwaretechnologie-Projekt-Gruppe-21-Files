package hotel.season;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

class SeasonTests {

    @Test
    void testSeason() {

        LocalDate start = LocalDate.now().plusMonths(1);
        LocalDate end = start.plusMonths(1);

        Season season = new Season("Test", 40, start, end);

        assertNotNull(season.getId());
        assertEquals("Test", season.getName());
        assertEquals(40, season.getPercentage());
        assertEquals(start, season.getStart());
        assertEquals(end, season.getEnd());
        assertEquals("Test", season.toString());
    }

    @Test
    void testSeasonForm() {

        String start = LocalDate.now().plusMonths(1).toString();
        String end = LocalDate.now().plusMonths(2).toString();

        SeasonForm form = new SeasonForm("Test", "80", start, end);

        assertEquals("Test", form.getName());
        assertEquals("80", form.getValue());
        assertEquals(start, form.getStart());
        assertEquals(end, form.getEnd());
    }
}
