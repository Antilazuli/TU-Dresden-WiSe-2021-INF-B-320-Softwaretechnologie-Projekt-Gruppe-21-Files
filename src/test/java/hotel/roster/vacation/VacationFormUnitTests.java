package hotel.roster.vacation;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class VacationFormUnitTests {

    @Test
    void vacationForm() {

        VacationForm form = new VacationForm("2022-05-21", "2022-05-25", "test");

        assertEquals("2022-05-21", form.getStart());
        assertEquals("2022-05-25", form.getEnd());
        assertEquals("test", form.getComment());
    }

}
