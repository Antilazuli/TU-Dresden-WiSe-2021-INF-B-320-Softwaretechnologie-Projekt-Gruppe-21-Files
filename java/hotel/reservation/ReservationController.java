package hotel.reservation;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;

import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.web.LoggedIn;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import hotel.booking.Booking;
import hotel.booking.BookingRepository;
import hotel.booking.Booking.Board;
import hotel.member.MemberManagement;
import hotel.member.MemberRepository;
import hotel.member.guest.Guest;
import hotel.room.Room;
import hotel.room.RoomCatalog;
import hotel.room.RoomCopy;
import hotel.room.PriceForm;

@PreAuthorize("hasRole('GUEST')")
@Controller
public class ReservationController {

    private final ReservationRepository reservations;
    private final MemberRepository members;
    private final BookingRepository bookings;
    private final RoomCatalog rooms;

    private static final String CART_ROUTE = "redirect:/rooms/cart";

    public ReservationController(ReservationRepository reservations, MemberRepository members,
            BookingRepository bookings, RoomCatalog rooms) {

        this.reservations = reservations;
        this.members = members;
        this.bookings = bookings;
        this.rooms = rooms;
    }

    @ModelAttribute("reservation")
    public Reservation initializeReservation(@LoggedIn Optional<UserAccount> userAccount) {

        if (userAccount.isEmpty()) {
            throw new IllegalAccessError("No userAccount!");
        }

        Guest guest = (Guest) members.findByUserAccount(userAccount.get());

        return reservations.findByGuest(guest)
                .orElse(new Reservation(guest, LocalDate.now(), LocalDate.now().plusDays(1)));
    }

    @GetMapping("/rooms/cart")
    public String cart(@ModelAttribute Reservation reservation, Model model) {

        model.addAttribute("reservation", reservation);
        model.addAttribute("boards", Board.values());

        return MemberManagement.GUEST_ROUTE + "roomsCart";
    }

    @PostMapping("/addRoom/{room}")
    public String add(@PathVariable Room room, @ModelAttribute Reservation reservation, PriceForm form) {

        LocalDate start = form.getStart();
        LocalDate end = form.getEnd();

        if (reservation.getRooms().isEmpty()) {
            reservation.setStart(start);
            reservation.setEnd(end);
        } else {
            Assert.isTrue(start.equals(reservation.getStart()) && end.equals(reservation.getEnd()),
                    "Dates may not be changed!");
        }

        RoomCopy roomCopy = rooms.save(new RoomCopy(room, form.getPrice()));
        reservation.addRoom(roomCopy);
        reservations.save(reservation);
        return CART_ROUTE;
    }

    @PostMapping("/removeRoom/{room}")
    public String remove(@ModelAttribute Reservation reservation, @PathVariable RoomCopy room) {

        reservation.removeRoom(room);
        reservations.save(reservation);
        rooms.delete(room);

        return CART_ROUTE;
    }

    @PostMapping("/removeAllRooms")
    public String removeAllRooms(@ModelAttribute Reservation reservation) {

        Set<RoomCopy> copies = Set.copyOf(reservation.getRooms());
        reservation.removeAllRooms();
        reservations.save(reservation);
        rooms.deleteAll(copies);

        return CART_ROUTE;
    }

    @PostMapping("/book")
    public String book(@ModelAttribute Reservation reservation) {

        if (reservation.getRooms().isEmpty()) {
            throw new IllegalStateException("No rooms selected!");
        }

        Booking booking = Booking.of(reservation);

        reservations.delete(reservation);

        booking = bookings.save(booking);

        return "redirect:/booking/" + booking.getId();
    }

    @PostMapping("/setBoard")
    public String setBoard(@ModelAttribute Reservation reservation, String board) {

        try {
            reservation.setBoard(Board.valueOf(board.toUpperCase()));
            reservations.save(reservation);
        } catch (Exception e) {
            // board not found
        }

        return CART_ROUTE;
    }
}
