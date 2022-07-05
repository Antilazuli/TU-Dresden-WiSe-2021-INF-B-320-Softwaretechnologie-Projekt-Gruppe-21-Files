package hotel.reservation;

import java.time.LocalDate;
import java.util.Set;

import org.jmolecules.event.types.DomainEvent;

import hotel.room.RoomCopy;

/**
 * Events published by the reservation module.
 *
 * @author Johannes Pforr
 */
public class ReservationEvents {

    public static class AddRoom implements DomainEvent {

        private RoomCopy room;
        private LocalDate start;
        private LocalDate end;

        public AddRoom(RoomCopy room, LocalDate start, LocalDate end) {

            this.room = room;
            this.start = start;
            this.end = end;
        }

        public static AddRoom of(Reservation reservation, RoomCopy room) {
            return new AddRoom(room, reservation.getStart(), reservation.getEnd());
        }

        public RoomCopy getRoom() {
            return room;
        }

        public LocalDate getStart() {
            return start;
        }

        public LocalDate getEnd() {
            return end;
        }

        @Override
        public String toString() {
            return "AddRoom";
        }
    }

    public static class RemoveRoom implements DomainEvent {

        private RoomCopy room;
        private LocalDate start;
        private LocalDate end;

        public RemoveRoom(RoomCopy room, LocalDate start, LocalDate end) {

            this.room = room;
            this.start = start;
            this.end = end;
        }

        public static RemoveRoom of(Reservation reservation, RoomCopy room) {
            return new RemoveRoom(room, reservation.getStart(), reservation.getEnd());
        }

        public RoomCopy getRoom() {
            return room;
        }

        public LocalDate getStart() {
            return start;
        }

        public LocalDate getEnd() {
            return end;
        }

        @Override
        public String toString() {
            return "RemoveRoom";
        }
    }

    public static class RemoveAllRooms implements DomainEvent {

        private final Set<RoomCopy> rooms;
        private final LocalDate start;
        private final LocalDate end;

        public RemoveAllRooms(Set<RoomCopy> rooms, LocalDate start, LocalDate end) {

            this.rooms = rooms;
            this.start = start;
            this.end = end;
        }

        public static RemoveAllRooms of(Reservation reservation, Set<RoomCopy> rooms) {
            return new RemoveAllRooms(rooms, reservation.getStart(), reservation.getEnd());
        }

        public Set<RoomCopy> getRooms() {
            return rooms;
        }

        public LocalDate getStart() {
            return start;
        }

        public LocalDate getEnd() {
            return end;
        }

        @Override
        public String toString() {
            return "RemoveAllRooms";
        }
    }
}
