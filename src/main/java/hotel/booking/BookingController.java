package hotel.booking;

import java.util.Optional;

import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.web.LoggedIn;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import hotel.member.MemberManagement;
import hotel.member.guest.Guest;

@PreAuthorize("hasRole('GUEST')")
@Controller
public class BookingController {

    private static final String BOOKINGS_STR = "bookings";

    private final MemberManagement management;
    private final BookingRepository bookings;

    public BookingController(MemberManagement management, BookingRepository bookings) {

        Assert.notNull(management, "MemberManagement must not be null!");
        Assert.notNull(bookings, "BookingRepository must not be null!");

        this.management = management;
        this.bookings = bookings;
    }

    @GetMapping("/bookings")
    public String bookings(Model model, @LoggedIn Optional<UserAccount> userAccount) {

        if (userAccount.isPresent()) {

            Guest guest = (Guest) management.findByUserAccount(userAccount.get());

            model.addAttribute(BOOKINGS_STR, bookings.findAllByGuest(guest));
        }

        return MemberManagement.GUEST_ROUTE + BOOKINGS_STR;
    }

    @GetMapping("/booking/{booking}")
    public String details(@PathVariable Booking booking, Model model) {

        model.addAttribute("booking", booking);
        model.addAttribute("rooms", booking.getRooms());
        model.addAttribute("orders", booking.getOrders());
        model.addAttribute("cancelable", booking.isCancelable());

        return MemberManagement.GUEST_ROUTE + "bookingDetails";
    }

    @PostMapping("/cancelBooking/{booking}")
    public String cancelBooking(@PathVariable Booking booking) {

        if (booking.cancel()) {
            bookings.save(booking);
        } else {
            throw new IllegalStateException("Booking is not cancelable!. Current State: " + booking.getState());
        }

        return "redirect:/booking/" + booking.getId();
    }

    @PreAuthorize("hasRole('MANAGER')")
    @GetMapping("/manager/bookings")
    public String bookings(Model model) {

        model.addAttribute(BOOKINGS_STR, bookings.findAll());

        return "manager/bookings";
    }

    @PreAuthorize("hasRole('MANAGER')")
    @GetMapping("/manager/booking/{booking}")
    public String detailsManager(@PathVariable Booking booking, Model model) {

        model.addAttribute("booking", booking);
        model.addAttribute("rooms", booking.getRooms());
        model.addAttribute("orders", booking.getOrders());

        return "manager/bookingDetails";
    }
}
