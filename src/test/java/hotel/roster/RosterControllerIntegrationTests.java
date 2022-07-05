package hotel.roster;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;

import hotel.AbstractIntegrationTests;
import hotel.member.MemberRepository;
import hotel.member.staff.Staff;

public class RosterControllerIntegrationTests extends AbstractIntegrationTests {

    @Autowired
    RosterController controller;

    @Autowired
    MemberRepository members;

    @Test
    @WithMockUser(roles = "STAFF")
    void rosterStaff() {

        Model model = new ExtendedModelMap();

        Staff staff = (Staff) members.findByEmail("peter@hotel.de").get();

        String returnedView = controller.rosterStaff(model, Optional.of(staff.getUserAccount()));

        assertEquals("staff/roster", returnedView);
    }

    @Test
    @WithMockUser(roles = "STAFF")
    void rosterStaffEmpty() {

        Model model = new ExtendedModelMap();

        String returnedView = controller.rosterStaff(model, Optional.empty());

        assertEquals("staff/roster", returnedView);
    }

    @Test
    @WithMockUser(roles = "MANAGER")
    void rosterManager() {

        Model model = new ExtendedModelMap();

        String returnedView = controller.rosterManager(model);

        assertEquals("manager/roster", returnedView);
    }
}