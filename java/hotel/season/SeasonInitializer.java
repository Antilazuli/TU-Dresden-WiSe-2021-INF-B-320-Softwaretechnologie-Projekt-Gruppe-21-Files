package hotel.season;

import java.time.LocalDate;

import org.salespointframework.core.DataInitializer;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Component
@Order(40)
class SeasonInitializer implements DataInitializer {

    private final SeasonRepository seasons;

    SeasonInitializer(SeasonRepository seasons) {

        Assert.notNull(seasons, "SeasonRepository must not be null!");

        this.seasons = seasons;
    }

    @Override
    public void initialize() {
        seasons.save(new Season("Start", 50, LocalDate.now(), LocalDate.now().plusDays(1)));
    }
}
