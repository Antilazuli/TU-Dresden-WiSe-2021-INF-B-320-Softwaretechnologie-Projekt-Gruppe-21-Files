package hotel.member;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import hotel.booking.BookingEvents.CheckInBooking;
import hotel.booking.BookingEvents.CheckOutBooking;

@Component
public class MemberListeners {

    private final MemberManagement management;

    public MemberListeners(MemberManagement management) {

        this.management = management;
    }

    @EventListener
    public void on(CheckInBooking event) {
        management.checkInGuest(event.getBooking().getGuest());
    }

    @EventListener
    public void on(CheckOutBooking event) {
        management.checkOutGuest(event.getBooking().getGuest());
    }
}
