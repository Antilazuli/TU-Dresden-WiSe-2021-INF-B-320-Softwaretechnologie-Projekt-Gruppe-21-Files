package hotel.reservation;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import javax.money.MonetaryAmount;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;

import org.javamoney.moneta.Money;
import org.salespointframework.core.AbstractAggregateRoot;
import org.salespointframework.core.Currencies;
import org.salespointframework.core.SalespointIdentifier;

import hotel.booking.Booking.Board;
import hotel.member.guest.Guest;
import hotel.reservation.ReservationEvents.AddRoom;
import hotel.reservation.ReservationEvents.RemoveAllRooms;
import hotel.reservation.ReservationEvents.RemoveRoom;
import hotel.room.Room;
import hotel.room.RoomCopy;

@Entity(name = "Reservation")
public class Reservation extends AbstractAggregateRoot<SalespointIdentifier> {

    private @Id SalespointIdentifier id;

    @OneToOne
    private Guest guest;

    @ManyToMany
    private Set<RoomCopy> rooms;

    private LocalDate start;
    private LocalDate end;

    private Board board;

    public Reservation() {
    }

    public Reservation(Guest guest, LocalDate start, LocalDate end) {

        this.id = new SalespointIdentifier();
        this.guest = guest;
        this.start = start;
        this.end = end;
        this.board = Board.HALF_BOARD;

        rooms = new HashSet<>();
    }

    public SalespointIdentifier getId() {
        return id;
    }

    public Guest getGuest() {
        return guest;
    }

    public LocalDate getStart() {
        return start;
    }

    public void setStart(LocalDate start) {

        if (rooms.isEmpty()) {
            this.start = start;
        } else {
            throw new IllegalStateException("Dates may not be changed!");
        }
    }

    public LocalDate getEnd() {
        return end;
    }

    public void setEnd(LocalDate end) {

        if (rooms.isEmpty()) {
            this.end = end;
        } else {
            throw new IllegalStateException("Dates may not be changed!");
        }
    }

    public LocalDate[] getDateSpan() {
        return start.datesUntil(end.plusDays(1)).toArray(LocalDate[]::new);
    }

    public long getNightCount() {
        return start.datesUntil(end).count();
    }

    public boolean addRoom(RoomCopy room) {

        registerEvent(AddRoom.of(this, room));

        return rooms.add(room);
    }

    public boolean removeRoom(RoomCopy room) {

        registerEvent(RemoveRoom.of(this, room));

        return rooms.remove(room);
    }

    public void removeAllRooms() {

        registerEvent(RemoveAllRooms.of(this, Set.copyOf(rooms)));

        rooms.clear();
    }

    public Set<RoomCopy> getRooms() {
        return rooms;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public Board getBoard() {
        return board;
    }

    public boolean isHalf() {
        return board.isHalf();
    }

    public MonetaryAmount getPricePerNight() {

        MonetaryAmount price = Money.of(0, Currencies.EURO);

        for (Room room : rooms) {
            price = price.add(room.getPrice());
        }

        return price;
    }

    public MonetaryAmount getBoardPrice() {
        return board.getPrice().multiply(getNightCount() * rooms.size());
    }

    public MonetaryAmount getTotal() {
        return getPricePerNight().multiply(getNightCount()).add(getBoardPrice());
    }
}
