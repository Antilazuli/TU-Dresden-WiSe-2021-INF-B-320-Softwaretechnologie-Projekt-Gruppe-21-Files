package hotel.member.staff;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;

import hotel.AbstractIntegrationTests;
import hotel.member.MemberForm;
import hotel.member.MemberRepository;
import hotel.member.RegistrationForm;
import hotel.member.guest.Guest;

public class StaffControllerIntegrationTests extends AbstractIntegrationTests {

    @Autowired
    StaffController controller;

    @Autowired
    MemberRepository members;

    @Test
    @WithMockUser(roles = "STAFF")
    void staffEmpty() {

        Model model = new ExtendedModelMap();

        String returnedView = controller.staff(model, Optional.empty());

        assertEquals("staff/staff", returnedView);
    }

    @Test
    @WithMockUser(roles = "STAFF")
    void staff() {

        Model model = new ExtendedModelMap();

        Staff staff = (Staff) members.findByEmail("peter@hotel.de").get();

        String returnedView = controller.staff(model, Optional.of(staff.getUserAccount()));

        assertEquals("staff/staff", returnedView);
    }

    @Test
    @WithMockUser(roles = "STAFF")
    void updateStaffEmpty() {

        Model model = new ExtendedModelMap();

        String returnedView = controller.updateStaff(model, Optional.empty(), mock(MemberForm.class));

        assertEquals("redirect:/", returnedView);
    }

    @Test
    @WithMockUser(roles = "STAFF")
    void updateStaff() {

        Model model = new ExtendedModelMap();

        Staff staff = (Staff) members.findByEmail("peter@hotel.de").get();

        String returnedView = controller.updateStaff(model, Optional.of(staff.getUserAccount()),
                new MemberForm("Pete", "Lustig", "123"));

        assertEquals("redirect:/", returnedView);
    }

    @Test
    @WithMockUser(roles = "MANAGER")
    void staffs() {

        Model model = new ExtendedModelMap();

        String returnedView = controller.staffs(model);

        assertEquals("manager/staffs", returnedView);
    }

    @Test
    @WithMockUser(roles = "MANAGER")
    void addStaff() {

        Model model = new ExtendedModelMap();

        String returnedView = controller.addStaff(mock(RegistrationForm.class), model);

        assertEquals("manager/addStaff", returnedView);
    }

    @Test
    @WithMockUser(roles = "MANAGER")
    void addStaffForm() {

        String returnedView = controller.addStaff(RegistrationForm.of("Test", "Tester", "tester@hotel.de", "123"));

        assertEquals("redirect:/manager/staffs", returnedView);
    }

    @Test
    @WithMockUser(roles = "MANAGER")
    void removeStaffEmpty() {

        Model model = new ExtendedModelMap();

        String returnedView = controller.removeStaff("0000", model);

        assertEquals("redirect:/manager/staffs", returnedView);
    }

    @Test
    @WithMockUser(roles = "MANAGER")
    void removeStaffWithGuest() {

        Model model = new ExtendedModelMap();

        Guest guest = (Guest) members.findByEmail("mika@hotel.de").get();

        String returnedView = controller.removeStaff(guest.getId().toString(), model);

        assertEquals("redirect:/manager/staffs", returnedView);
    }

    @Test
    @WithMockUser(roles = "MANAGER")
    void removeStaffWithStaff() {

        Model model = new ExtendedModelMap();

        Staff staff = (Staff) members.findByEmail("peter@hotel.de").get();

        String returnedView = controller.removeStaff(staff.getId().toString(), model);

        assertEquals("redirect:/manager/staffs", returnedView);
    }
}
