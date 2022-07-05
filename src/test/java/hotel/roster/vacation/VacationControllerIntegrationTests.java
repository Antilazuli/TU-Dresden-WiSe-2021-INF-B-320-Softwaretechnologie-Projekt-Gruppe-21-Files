package hotel.roster.vacation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;

import hotel.AbstractIntegrationTests;
import hotel.member.MemberRepository;
import hotel.member.staff.Staff;

public class VacationControllerIntegrationTests extends AbstractIntegrationTests {

    @Autowired
    VacationController controller;

    @Autowired
    MemberRepository members;

    @Test
    @WithMockUser(roles = "STAFF")
    void vacationRequestStaff() {

        Model model = new ExtendedModelMap();

        Staff staff = (Staff) members.findByEmail("peter@hotel.de").get();

        String returnedView = controller.vacationRequestsStaff(model, Optional.of(staff.getUserAccount()));

        assertEquals("staff/vacationRequests", returnedView);
    }

    @Test
    @WithMockUser(roles = "STAFF")
    void vacationRequestStaffEmpty() {

        Model model = new ExtendedModelMap();

        String returnedView = controller.vacationRequestsStaff(model, Optional.empty());

        assertEquals("staff/vacationRequests", returnedView);
    }

    @Test
    @WithMockUser(roles = "STAFF")
    void newVacationRequest() {

        Model model = new ExtendedModelMap();

        String returnedView = controller.newVacationRequest(model, mock(VacationForm.class));

        assertEquals("staff/vacationRequest", returnedView);
    }

    @Test
    @WithMockUser(roles = "STAFF")
    void addVacationRequest() {

        Staff staff = (Staff) members.findByEmail("peter@hotel.de").get();

        String returnedView = controller.addVacationRequest(staff.getUserAccount(),
                new VacationForm("2022-05-21", "2022-05-25", "test"), mock(Errors.class));

        assertEquals("redirect:/staff/vacation-requests", returnedView);
    }

    @Test
    @WithMockUser(roles = "STAFF")
    void addVacationRequestErrors() {

        Errors errors = mock(Errors.class);
        when(errors.hasErrors()).thenReturn(true);

        Staff staff = (Staff) members.findByEmail("peter@hotel.de").get();

        String returnedView = controller.addVacationRequest(staff.getUserAccount(),
                new VacationForm("2022-05-21", "2022-05-25", "test"), errors);

        assertEquals("staff/vacationRequest", returnedView);
    }

    @Test
    @WithMockUser(roles = "STAFF")
    void addVacationRequestDateError() {

        Errors errors = mock(Errors.class);
        when(errors.hasErrors()).thenReturn(true);

        Staff staff = (Staff) members.findByEmail("peter@hotel.de").get();

        String returnedView = controller.addVacationRequest(staff.getUserAccount(),
                new VacationForm("2022-05-21", "x", "test"), errors);

        assertEquals("staff/vacationRequest", returnedView);
    }

    @Test
    @WithMockUser(roles = "STAFF")
    void addVacationRequestCommentBlank() {

        Staff staff = (Staff) members.findByEmail("peter@hotel.de").get();

        String returnedView = controller.addVacationRequest(staff.getUserAccount(),
                new VacationForm("2022-05-21", "2022-05-25", "   "), mock(Errors.class));

        assertEquals("redirect:/staff/vacation-requests", returnedView);
    }

    @Test
    @WithMockUser(roles = "MANAGER")
    void vacationRequestsManager() {

        Model model = new ExtendedModelMap();

        String returnedView = controller.vacationRequestsManager(model);

        assertEquals("manager/vacationRequests", returnedView);
    }

    @Test
    @WithMockUser(roles = "MANAGER")
    void details() {

        Model model = new ExtendedModelMap();

        String returnedView = controller.details(mock(VacationRequest.class), model);

        assertEquals("manager/vacationRequestDetails", returnedView);
    }

    @Test
    @WithMockUser(roles = "MANAGER")
    void acceptRequest() {

        Staff staff = (Staff) members.findByEmail("peter@hotel.de").get();

        VacationRequest vacationRequest = new VacationRequest(staff, LocalDate.now().plusDays(3),
                LocalDate.now().plusDays(5), "test");

        String returnedView = controller.acceptRequest(vacationRequest);

        assertEquals("redirect:/manger/vacation-requests", returnedView);
    }

    @Test
    @WithMockUser(roles = "MANAGER")
    void acceptRequestError() {

        VacationRequest vacationRequest = mock(VacationRequest.class);
        when(vacationRequest.accept()).thenReturn(false);

        assertThrows(IllegalStateException.class, () -> controller.acceptRequest(vacationRequest));
    }

    @Test
    @WithMockUser(roles = "MANAGER")
    void declineRequest() {

        Staff staff = (Staff) members.findByEmail("peter@hotel.de").get();

        VacationRequest vacationRequest = new VacationRequest(staff, LocalDate.now().plusDays(3),
                LocalDate.now().plusDays(5), "test");

        String returnedView = controller.declineRequest(vacationRequest);

        assertEquals("redirect:/manger/vacation-requests", returnedView);
    }

    @Test
    @WithMockUser(roles = "MANAGER")
    void declineRequestError() {

        VacationRequest vacationRequest = mock(VacationRequest.class);
        when(vacationRequest.decline()).thenReturn(false);

        assertThrows(IllegalStateException.class, () -> controller.declineRequest(vacationRequest));
    }
}
