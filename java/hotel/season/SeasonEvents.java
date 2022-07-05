package hotel.season;

import org.jmolecules.event.types.DomainEvent;

/**
 * Events published by the season module.
 *
 * @author Johannes Pforr
 */
public class SeasonEvents {

    public static class CreateSeason implements DomainEvent {

        private Season season;

        public CreateSeason(Season season) {

            this.season = season;
        }

        public static CreateSeason of(Season season) {
            return new CreateSeason(season);
        }

        public Season getSeason() {
            return season;
        }

        @Override
        public String toString() {
            return "CreateSeason";
        }
    }

    public static class DeleteSeason implements DomainEvent {

        private Season season;

        public DeleteSeason(Season season) {

            this.season = season;
        }

        public static DeleteSeason of(Season season) {
            return new DeleteSeason(season);
        }

        public Season getSeason() {
            return season;
        }

        @Override
        public String toString() {
            return "DeleteSeason";
        }
    }
}
