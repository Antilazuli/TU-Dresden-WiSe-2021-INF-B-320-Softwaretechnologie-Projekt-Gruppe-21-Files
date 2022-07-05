package hotel.season;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.Id;

import org.salespointframework.core.AbstractAggregateRoot;
import org.salespointframework.core.SalespointIdentifier;

import hotel.season.SeasonEvents.CreateSeason;
import hotel.season.SeasonEvents.DeleteSeason;

@Entity
public class Season extends AbstractAggregateRoot<SalespointIdentifier> {

    private @Id SalespointIdentifier id;

    private LocalDate start;
    private LocalDate end;

    private String name;

    private int percentage;

    @SuppressWarnings("unused")
    private Season() {
    }

    public Season(String name, int percentage, LocalDate start, LocalDate end) {

        this.name = name;
        this.percentage = percentage;
        this.start = start;
        this.end = end;

        id = new SalespointIdentifier();

        registerEvent(CreateSeason.of(this));
    }

    @Override
    public SalespointIdentifier getId() {
        return id;
    }

    public String getName() {

        return name;
    }

    public int getPercentage() {
        return percentage;
    }

    public LocalDate getStart() {

        return start;
    }

    public LocalDate getEnd() {

        return end;
    }

    public Season delete() {

        registerEvent(DeleteSeason.of(this));

        return this;
    }

    @Override
    public String toString() {
        return name;
    }
}
