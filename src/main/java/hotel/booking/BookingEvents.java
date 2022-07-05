package hotel.booking;

import org.jmolecules.event.types.DomainEvent;

/**
 * Events published by the booking module.
 *
 * @author Johannes Pforr
 */
public class BookingEvents {

    public static class CreateBooking implements DomainEvent {

        private final Booking booking;

        public CreateBooking(Booking booking) {
            this.booking = booking;
        }

        public static CreateBooking of(Booking booking) {
            return new CreateBooking(booking);
        }

        public Booking getBooking() {
            return booking;
        }

        @Override
        public String toString() {
            return "CreateBooking";
        }
    }

    public static class CancelBooking implements DomainEvent {

        private final Booking booking;

        public CancelBooking(Booking booking) {
            this.booking = booking;
        }

        public static CancelBooking of(Booking booking) {
            return new CancelBooking(booking);
        }

        public Booking getBooking() {
            return booking;
        }

        @Override
        public String toString() {
            return "CancelBooking";
        }
    }

    public static class CheckInBooking implements DomainEvent {

        private final Booking booking;

        public CheckInBooking(Booking booking) {
            this.booking = booking;
        }

        public static CheckInBooking of(Booking booking) {
            return new CheckInBooking(booking);
        }

        public Booking getBooking() {
            return booking;
        }

        @Override
        public String toString() {
            return "CheckInBooking";
        }
    }

    public static class CheckOutBooking implements DomainEvent {

        private final Booking booking;

        public CheckOutBooking(Booking booking) {
            this.booking = booking;
        }

        public static CheckOutBooking of(Booking booking) {
            return new CheckOutBooking(booking);
        }

        public Booking getBooking() {
            return booking;
        }

        @Override
        public String toString() {
            return "CheckOutBooking";
        }
    }
}
