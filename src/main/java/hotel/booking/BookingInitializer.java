package hotel.booking;

import java.time.LocalDate;

import org.salespointframework.core.DataInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import hotel.member.MemberManagement;
import hotel.member.guest.Guest;
import hotel.reservation.Reservation;
import hotel.reservation.ReservationRepository;
import hotel.room.Room;
import hotel.room.RoomCatalog;
import hotel.room.RoomCopy;

@Component
@Order(50)
class BookingInitializer implements DataInitializer {

    private static final Logger LOG = LoggerFactory.getLogger(BookingInitializer.class);

    private final MemberManagement management;
    private final RoomCatalog rooms;
    private final ReservationRepository reservations;
    private final BookingRepository bookings;

    BookingInitializer(MemberManagement management, RoomCatalog rooms, ReservationRepository reservations,
            BookingRepository bookings) {

        Assert.notNull(management, "MemberManagement must not be null!");
        Assert.notNull(rooms, "RoomCatalog must not be null!");
        Assert.notNull(reservations, "ReservationRepository must not be null!");
        Assert.notNull(bookings, "BookingRepository must not be null!");

        this.management = management;
        this.rooms = rooms;
        this.reservations = reservations;
        this.bookings = bookings;
    }

    @Override
    public void initialize() {

        LOG.info("Creating test booking.");

        Guest guest = management.findAllGuests().iterator().next();

        LocalDate start = LocalDate.now().minusDays(1);
        LocalDate end = LocalDate.now().plusDays(6);

        Reservation reservation = new Reservation(guest, start, end);

        Room room = rooms.findAll().iterator().next();

        RoomCopy roomCopy = rooms.save(new RoomCopy(room, room.getPrice()));

        reservation.addRoom(roomCopy);

        reservations.save(reservation);

        Booking booking = Booking.of(reservation);

        reservations.delete(reservation);

        bookings.save(booking);

        // CheckedOut - Booking
        LOG.info("Creating test checkedOut booking.");

        Guest guest2 = (Guest) management.findByEmail("mika@hotel.de").get();

        LocalDate start2 = LocalDate.now().minusDays(25);
        LocalDate end2 = LocalDate.now().minusDays(20);

        Reservation reservation2 = new Reservation(guest2, start2, end2);

        Room room2 = rooms.findAll().iterator().next();

        RoomCopy roomCopy2 = rooms.save(new RoomCopy(room2, room2.getPrice()));

        reservation2.addRoom(roomCopy2);

        reservations.save(reservation2);

        Booking booking2 = Booking.of(reservation2);

        reservations.delete(reservation2);

        bookings.save(booking2);
    }
}
