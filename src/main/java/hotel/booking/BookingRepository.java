package hotel.booking;

import java.time.LocalDate;

import org.salespointframework.core.SalespointIdentifier;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.util.Streamable;

import hotel.booking.Booking.State;
import hotel.member.guest.Guest;

public interface BookingRepository extends CrudRepository<Booking, SalespointIdentifier> {

    Streamable<Booking> findAllByGuest(Guest guest);

    Streamable<Booking> findAllByState(State state);

    Streamable<Booking> findAllByGuestAndState(Guest guest, State state);

    @Query("select b from Booking b where :date between b.start and b.end")
    Streamable<Booking> findAllByDate(LocalDate date);
}
