package hotel.roster.vacation;

import org.jmolecules.event.types.DomainEvent;

/**
 * Events published by the roster module.
 *
 * @author Johannes Pforr
 */
public class VacationEvents {

    public static class AcceptVacation implements DomainEvent {

        private final VacationRequest vacationRequest;

        public AcceptVacation(VacationRequest vacationRequest) {
            this.vacationRequest = vacationRequest;
        }

        public static AcceptVacation of(VacationRequest vacationRequest) {
            return new AcceptVacation(vacationRequest);
        }

        public VacationRequest getVacationRequest() {
            return vacationRequest;
        }

        @Override
        public String toString() {
            return "AcceptVacation";
        }
    }
}
