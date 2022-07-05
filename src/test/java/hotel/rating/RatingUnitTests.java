package hotel.rating;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

public class RatingUnitTests {

    @Test
    void rating() {

        int starRating = 4;
        String text = "";
        LocalDateTime dateTime = LocalDateTime.now();

        Rating rating = new Rating(starRating, text, dateTime);

        String date = "Bewertung vom " + dateTime.getDayOfMonth() + "." + dateTime.getMonthValue() + "."
                + dateTime.getYear();

        assertEquals(4, rating.getRating());
        assertEquals("", rating.getText());
        assertEquals(dateTime, rating.getDateTime());
        assertEquals(date, rating.getDate());
    }

}
