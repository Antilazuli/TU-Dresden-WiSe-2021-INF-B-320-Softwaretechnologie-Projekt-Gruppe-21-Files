package hotel.rating;

import java.time.LocalDateTime;

import org.salespointframework.core.DataInitializer;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Component
@Order(60)
class RatingInitializer implements DataInitializer {

    private final RatingRepository ratings;

    RatingInitializer(RatingRepository ratings) {

        Assert.notNull(ratings, "RatingRepository must not be null!");

        this.ratings = ratings;
    }

    @Override
    public void initialize() {

        LocalDateTime dateTime1 = LocalDateTime.now().minusDays(5);
        LocalDateTime dateTime2 = LocalDateTime.now().minusDays(3);

        Rating rating = new Rating(4, "Noice hier", dateTime1);
        Rating rating2 = new Rating(5, "blablabla", dateTime2);

        ratings.save(rating);
        ratings.save(rating2);

    }

}
