package hotel.roster;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import hotel.booking.BookingEvents.CancelBooking;
import hotel.booking.BookingEvents.CreateBooking;
import hotel.roster.vacation.VacationEvents.AcceptVacation;

@Component
public class RosterListeners {

    private final RosterManagement management;

    public RosterListeners(RosterManagement management) {

        this.management = management;
    }

    @EventListener
    public void on(CreateBooking event) {
        management.assignRooms(event.getBooking().getDateSpan(), event.getBooking().getOriginalRooms());
    }

    @EventListener
    public void on(CancelBooking event) {
        management.unassignRooms(event.getBooking().getDateSpan(), event.getBooking().getOriginalRooms());
    }

    @EventListener
    public void on(AcceptVacation event) {
        management.acceptVacationRequest(event.getVacationRequest());
    }
}
