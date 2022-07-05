package hotel.member.guest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;

import hotel.AbstractIntegrationTests;
import hotel.member.MemberRepository;

public class GuestControllerIntegrationTests extends AbstractIntegrationTests {

    @Autowired
    GuestController controller;

    @Autowired
    MemberRepository members;

    @Test
    @WithMockUser(roles = "GUEST")
    void guestEmpty() {

        Model model = new ExtendedModelMap();

        String returnedView = controller.guest(model, Optional.empty());

        assertEquals("guest/guest", returnedView);
    }

    @Test
    @WithMockUser(roles = "GUEST")
    void guest() {

        Model model = new ExtendedModelMap();

        Guest guest = (Guest) members.findByEmail("mika@hotel.de").get();

        String returnedView = controller.guest(model, Optional.of(guest.getUserAccount()));

        assertEquals("guest/guest", returnedView);
    }

    @Test
    @WithMockUser(roles = "GUEST")
    void updateGuestNewEmpty() {

        Model model = new ExtendedModelMap();

        String returnedView = controller.updateGuestNew(model, Optional.empty(), mock(GuestEditForm.class));

        assertEquals("guest/guestUpdate", returnedView);
    }

    @Test
    @WithMockUser(roles = "GUEST")
    void updateGuestNew() {

        Model model = new ExtendedModelMap();

        Guest guest = (Guest) members.findByEmail("mika@hotel.de").get();

        String returnedView = controller.updateGuestNew(model, Optional.of(guest.getUserAccount()),
                mock(GuestEditForm.class));

        assertEquals("guest/guestUpdate", returnedView);
    }

    @Test
    @WithMockUser(roles = "GUEST")
    void updateGuestDoneEmpty() {

        Model model = new ExtendedModelMap();

        String returnedView = controller.updateGuestDone(model, Optional.empty(), mock(GuestEditForm.class));

        assertEquals("redirect:/guest", returnedView);
    }

    @Test
    @WithMockUser(roles = "GUEST")
    void updateGuestDone() {

        Model model = new ExtendedModelMap();

        Guest guest = (Guest) members.findByEmail("mika@hotel.de").get();

        String returnedView = controller.updateGuestDone(model, Optional.of(guest.getUserAccount()),
                new GuestEditForm("mika", "tax", "12345678", "123456789", "123", "123"));

        assertEquals("redirect:/guest", returnedView);
    }

    @Test
    @WithMockUser(roles = "MANAGER")
    void guests() {

        Model model = new ExtendedModelMap();

        String returnedView = controller.guests(model);

        assertEquals("manager/guests", returnedView);
    }
}
