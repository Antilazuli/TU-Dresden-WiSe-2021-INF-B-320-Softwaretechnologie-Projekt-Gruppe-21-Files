package hotel.room;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import hotel.booking.BookingEvents.CancelBooking;
import hotel.reservation.ReservationEvents.AddRoom;
import hotel.reservation.ReservationEvents.RemoveAllRooms;
import hotel.reservation.ReservationEvents.RemoveRoom;
import hotel.season.SeasonEvents.CreateSeason;
import hotel.season.SeasonEvents.DeleteSeason;

@Component
public class InventoryListeners {

    private final RoomManagement management;

    public InventoryListeners(RoomManagement management) {

        this.management = management;
    }

    @EventListener
    public void on(AddRoom event) {
        management.decreaseStock(event.getRoom().getRoom(), event.getStart(), event.getEnd());
    }

    @EventListener
    public void on(RemoveRoom event) {
        management.increaseStock(event.getRoom().getRoom(), event.getStart(), event.getEnd());
    }

    @EventListener
    public void on(RemoveAllRooms event) {
        management.increaseStock(toOriginal(event.getRooms()), event.getStart(), event.getEnd());
    }

    @EventListener
    public void on(CancelBooking event) {
        management.increaseStock(event.getBooking().getOriginalRooms(), event.getBooking().getStart(),
                event.getBooking().getEnd());
    }

    @EventListener
    public void on(CreateSeason event) {
        management.updatePrice(event.getSeason().getStart(), event.getSeason().getEnd(),
                event.getSeason().getPercentage());
    }

    @EventListener
    public void on(DeleteSeason event) {
        management.updatePrice(event.getSeason().getStart(), event.getSeason().getEnd(), 1);
    }

    private Set<Room> toOriginal(Set<RoomCopy> copys) {
        return copys.stream().map(RoomCopy::getRoom).collect(Collectors.toSet());
    }
}
