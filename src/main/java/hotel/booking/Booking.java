package hotel.booking;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import javax.money.MonetaryAmount;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.javamoney.moneta.Money;
import org.salespointframework.core.AbstractAggregateRoot;
import org.salespointframework.core.Currencies;
import org.salespointframework.core.SalespointIdentifier;
import org.salespointframework.order.Order;

import hotel.booking.BookingEvents.CancelBooking;
import hotel.booking.BookingEvents.CheckInBooking;
import hotel.booking.BookingEvents.CheckOutBooking;
import hotel.booking.BookingEvents.CreateBooking;
import hotel.member.guest.Guest;
import hotel.reservation.Reservation;
import hotel.room.Room;
import hotel.room.RoomCopy;

@Entity
public class Booking extends AbstractAggregateRoot<SalespointIdentifier> {

    private @Id SalespointIdentifier id;

    @ManyToOne
    private Guest guest;

    @ManyToMany
    private Set<RoomCopy> rooms;

    private Board board;

    private LocalDate start;
    private LocalDate end;

    @OneToMany
    private Set<Order> orders;

    private State state;

    private LocalDateTime stateDate;

    public enum Board {
        HALF_BOARD {
            @Override
            public MonetaryAmount getPrice() {
                return Money.of(10, Currencies.EURO);
            }

            @Override
            public boolean isHalf() {
                return true;
            }

            @Override
            public String toString() {
                return "Halbpension";
            }
        },
        FULL_BOARD {
            @Override
            public MonetaryAmount getPrice() {
                return Money.of(20, Currencies.EURO);
            }

            @Override
            public String toString() {
                return "Vollpension";
            }
        };

        public abstract MonetaryAmount getPrice();

        public boolean isHalf() {
            return false;
        }
    }

    public enum State {
        BOOKED {
            @Override
            public String toString() {
                return "gebucht";
            }
        },
        CHECKED_IN {
            @Override
            public String toString() {
                return "eingecheckt";
            }
        },
        CHECKED_OUT {
            @Override
            public String toString() {
                return "ausgecheckt";
            }
        },
        CANCELED {
            @Override
            public String toString() {
                return "storniert";
            }
        };
    }

    @SuppressWarnings("unused")
    private Booking() {
    }

    public Booking(Guest guest, Set<RoomCopy> rooms, Board board, LocalDate start, LocalDate end) {

        this.guest = guest;
        this.rooms = rooms;
        this.board = board;
        this.start = start;
        this.end = end;

        id = new SalespointIdentifier();
        orders = new HashSet<>();

        setState(State.BOOKED);
        getState();

        registerEvent(CreateBooking.of(this));
    }

    public static Booking of(Reservation reservation) {

        Guest guest = reservation.getGuest();
        Set<RoomCopy> rooms = reservation.getRooms();
        Board catering = reservation.getBoard();
        LocalDate start = reservation.getStart();
        LocalDate end = reservation.getEnd();

        return new Booking(guest, rooms, catering, start, end);
    }

    public SalespointIdentifier getId() {
        return id;
    }

    public Guest getGuest() {
        return guest;
    }

    public Set<RoomCopy> getRooms() {
        return rooms;
    }

    public Set<Room> getOriginalRooms() {
        return rooms.stream().map(RoomCopy::getRoom).collect(Collectors.toSet());
    }

    public Board getBoard() {
        return board;
    }

    public LocalDate getStart() {
        return start;
    }

    public LocalDate getEnd() {
        return end;
    }

    public LocalDate[] getDateSpan() {
        return start.datesUntil(end.plusDays(1)).toArray(LocalDate[]::new);
    }

    public long getNightCount() {
        return start.datesUntil(end).count();
    }

    private void setState(State state) {
        this.state = state;
        stateDate = LocalDateTime.now();
    }

    public State getState() {

        if (state == State.BOOKED) {

            for (LocalDate date : getDateSpan()) {

                if (date.equals(LocalDate.now())) {
                    setState(State.CHECKED_IN);
                    registerEvent(CheckInBooking.of(this));
                    return state;
                }
            }

            if (LocalDate.now().isAfter(getEnd())) {
                setState(State.CHECKED_OUT);
                registerEvent(CheckOutBooking.of(this));
                return state;
            }
        }

        return state;
    }

    public LocalDateTime getStateDate() {
        return stateDate;
    }

    public boolean isCheckedIn() {
        return getState() == State.CHECKED_IN;
    }

    public boolean isCancelable() {
        return getState() == State.BOOKED;
    }

    public boolean cancel() {

        if (isCancelable()) {

            setState(State.CANCELED);

            registerEvent(CancelBooking.of(this));

            return true;
        }

        return false;
    }

    public Set<Order> getOrders() {
        return orders;
    }

    public boolean addOrder(Order order) {
        return orders.add(order);
    }

    public MonetaryAmount getOrdersTotal() {

        MonetaryAmount total = Money.of(0, Currencies.EURO);

        for (Order order : orders) {
            total = total.add(order.getTotal());
        }

        return total;
    }

    public MonetaryAmount getOrdersTotal(LocalDate date) {

        MonetaryAmount total = Money.of(0, Currencies.EURO);

        for (Order order : orders) {
            if (order.getDateCreated().toLocalDate().equals(date)) {
                total = total.add(order.getTotal());
            }
        }

        return total;
    }

    public MonetaryAmount getPricePerNight() {

        MonetaryAmount price = Money.of(0, Currencies.EURO);

        for (Room room : rooms) {
            price = price.add(room.getPrice());
        }

        return price;
    }

    public MonetaryAmount getBoardPricePerDay() {
        return board.getPrice().multiply(rooms.size());
    }

    public MonetaryAmount getBoardPrice() {
        return getBoardPricePerDay().multiply(getNightCount());
    }

    public MonetaryAmount getTotal() {

        if (getState() == State.CANCELED) {
            return Money.of(50, Currencies.EURO);
        }

        return getPricePerNight().multiply(getNightCount()).add(getBoardPrice()).add(getOrdersTotal());
    }

    public MonetaryAmount getTotal(LocalDate date) {

        if (getState() == State.CANCELED) {
            if (stateDate.toLocalDate().equals(date)) {
                return Money.of(50, Currencies.EURO);
            }
            return Money.of(0, Currencies.EURO);
        }

        return getPricePerNight().add(getBoardPricePerDay()).add(getOrdersTotal(date));
    }
}
