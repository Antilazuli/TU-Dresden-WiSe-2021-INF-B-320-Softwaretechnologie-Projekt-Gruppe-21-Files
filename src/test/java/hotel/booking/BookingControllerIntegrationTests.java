package hotel.booking;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;

import hotel.AbstractIntegrationTests;
import hotel.booking.Booking.State;
import hotel.member.MemberRepository;
import hotel.member.guest.Guest;

public class BookingControllerIntegrationTests extends AbstractIntegrationTests {

    @Autowired
    BookingController controller;

    @Autowired
    MemberRepository members;

    @Autowired
    BookingRepository bookings;

    @Test
    @WithMockUser(roles = "GUEST")
    void bookingsEmpty() {

        Model model = new ExtendedModelMap();

        String returnedView = controller.bookings(model, Optional.empty());

        assertEquals("guest/bookings", returnedView);
    }

    @Test
    @WithMockUser(roles = "GUEST")
    void bookingsGuest() {

        Model model = new ExtendedModelMap();

        Guest guest = (Guest) members.findByEmail("mika@hotel.de").get();

        String returnedView = controller.bookings(model, Optional.of(guest.getUserAccount()));

        assertEquals("guest/bookings", returnedView);
    }

    @Test
    @WithMockUser(roles = "GUEST")
    void detailsGuest() {

        Model model = new ExtendedModelMap();

        String returnedView = controller.details(bookings.findAllByState(State.CHECKED_OUT).toList().get(0), model);

        assertEquals("guest/bookingDetails", returnedView);
    }

    @Test
    @WithMockUser(roles = "MANAGER")
    void bookingsManager() {

        Model model = new ExtendedModelMap();

        String returnedView = controller.bookings(model);

        assertEquals("manager/bookings", returnedView);
    }

    @Test
    @WithMockUser(roles = "MANAGER")
    void detailsManager() {

        Model model = new ExtendedModelMap();

        String returnedView = controller.detailsManager(bookings.findAllByState(State.CHECKED_OUT).toList().get(0),
                model);

        assertEquals("manager/bookingDetails", returnedView);
    }
}
