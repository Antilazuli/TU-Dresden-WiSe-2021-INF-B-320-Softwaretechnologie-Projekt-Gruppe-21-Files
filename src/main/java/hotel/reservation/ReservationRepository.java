package hotel.reservation;

import java.util.Optional;

import org.salespointframework.core.SalespointIdentifier;
import org.springframework.data.repository.CrudRepository;

import hotel.member.guest.Guest;

public interface ReservationRepository extends CrudRepository<Reservation, SalespointIdentifier> {

    Optional<Reservation> findByGuest(Guest guest);
}
