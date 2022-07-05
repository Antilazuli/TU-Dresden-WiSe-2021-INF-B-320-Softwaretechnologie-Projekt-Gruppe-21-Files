package hotel.reservation;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.Random;

import org.junit.jupiter.api.Test;
import org.salespointframework.quantity.Quantity;
import org.springframework.beans.factory.annotation.Autowired;

import hotel.AbstractIntegrationTests;
import hotel.room.Room;
import hotel.room.RoomCatalog;
import hotel.room.RoomCopy;
import hotel.room.RoomInventory;

class ReservationIntegrationTests extends AbstractIntegrationTests {

    @Autowired
    RoomInventory inventory;
    @Autowired
    RoomCatalog catalog;
    @Autowired
    ReservationRepository reservations;

    @Test
    void reservRoom() {

        LocalDate start = LocalDate.now().plusDays(1);
        LocalDate end = start.plusDays(2);

        Room room = catalog.save(Room.getRandom(new Random(), 0));

        Reservation reservation = new Reservation(null, start, end);

        reservation.addRoom(catalog.save(new RoomCopy(room, room.getPrice())));

        reservations.save(reservation);

        var roomPerDays = inventory.findByProductBetweenDate(room.getId(), reservation.getStart(),
                reservation.getEnd());

        assertThat(roomPerDays).allMatch(it -> it.getQuantity().isEqualTo(Quantity.NONE)).hasSize(2);
    }
}
