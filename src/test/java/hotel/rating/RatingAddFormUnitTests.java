package hotel.rating;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class RatingAddFormUnitTests {

    @Test
    void ratingAddForm() {

        String star = "4";
        String comment = "";

        RatingAddForm form = new RatingAddForm(star, comment);

        assertEquals("4", form.getStar());
        assertEquals("", form.getComment());
    }
}
