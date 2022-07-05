package hotel.rating;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Rating {

    private @Id @GeneratedValue long id;

    private int starRating;
    private String text;

    private LocalDateTime dateTime;

    @SuppressWarnings("unused")
    private Rating() {
    }

    public Rating(int starRating, String text, LocalDateTime dateTime) {

        this.starRating = starRating;
        this.text = text;
        this.dateTime = dateTime;
    }

    public long getId() {
        return id;
    }

    public int getRating() {
        return starRating;
    }

    public String getText() {
        return text;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public String getDate() {
        return "Bewertung vom " + dateTime.getDayOfMonth() + "." + dateTime.getMonthValue() + "." + dateTime.getYear();
    }

}
