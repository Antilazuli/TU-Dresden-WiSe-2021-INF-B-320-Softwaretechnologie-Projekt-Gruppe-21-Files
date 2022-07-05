package hotel.roster.vacation;

import java.time.LocalDate;
import java.time.Period;
import java.util.Comparator;
import java.util.Optional;

import javax.validation.Valid;

import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.web.LoggedIn;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import hotel.member.MemberManagement;
import hotel.member.staff.Staff;

@Controller
public class VacationController {

    // private static final String START = "start";
    // private static final String END = "end";

    private final MemberManagement memberManagement;
    // private final RosterManagement rosterManagement;
    private final VacationRepository vacations;

    public VacationController(MemberManagement memberManagement, // RosterManagement rosterManagement,
            VacationRepository vacations) {

        Assert.notNull(memberManagement, "MemberManagement must not be null!");
        // Assert.notNull(rosterManagement, "RosterManagement must not be null!");
        Assert.notNull(vacations, "VacationRepository must not be null!");

        this.memberManagement = memberManagement;
        // this.rosterManagement = rosterManagement;
        this.vacations = vacations;
    }

    @PreAuthorize("hasRole('STAFF')")
    @GetMapping("/staff/vacation-requests")
    public String vacationRequestsStaff(Model model, @LoggedIn Optional<UserAccount> userAccount) {

        if (userAccount.isPresent()) {

            Staff staff = (Staff) memberManagement.findByUserAccount(userAccount.get());

            model.addAttribute("vacationRequests", vacations.findAllByStaff(staff));
        }
        return "staff/vacationRequests";
    }

    @PreAuthorize("hasRole('STAFF')")
    @GetMapping("/staff/vacation-request")
    public String newVacationRequest(Model model, VacationForm form) {
        return "staff/vacationRequest";
    }

    private static LocalDate toDate(String date) {

        try {
            return LocalDate.parse(date);
        } catch (Exception e) {
            return null;
        }
    }

    private void validateDates(Staff staff, LocalDate start, LocalDate end, Errors result) {

        var fault1 = start.isBefore(LocalDate.now());
        var fault2 = !end.isAfter(start);
        var fault3 = Period.between(start, end).toTotalMonths() > 12;
        var fault4 = false;

        if (!vacations.findAllActiveFromDate(staff, start).isEmpty()) {

            var minStart = vacations.findAllActiveFromDate(staff, start).stream()
                    .min(Comparator.comparing(VacationRequest::getStart))
                    .orElseThrow().getStart();
            var maxEnd = vacations.findAllActiveFromDate(staff, start).stream()
                    .max(Comparator.comparing(VacationRequest::getEnd))
                    .orElseThrow().getEnd();

            fault4 = start.compareTo(maxEnd) <= 0 && minStart.compareTo(end) <= 0;
        }

        if (fault1 || fault2 || fault3 || fault4) {
            result.rejectValue("end", "1", "Invalides Datum!");
        }
    }

    @PreAuthorize("hasRole('STAFF')")
    @PostMapping("/staff/addVacationRequest")
    public String addVacationRequest(@LoggedIn UserAccount userAccount, @Valid VacationForm form, Errors result) {

        var staff = (Staff) memberManagement.findByUserAccount(userAccount);

        var start = toDate(form.getStart());
        var end = toDate(form.getEnd());

        if (start == null || end == null) {
            result.rejectValue("end", "0", "Invalides Datum!");
        } else {
            validateDates(staff, start, end, result);
        }

        if (result.hasErrors()) {
            return "staff/vacationRequest";
        }

        String comment = form.getComment();
        if (comment.isBlank()) {
            comment = "";
        }

        vacations.save(new VacationRequest(staff, start, end, comment));

        return "redirect:/staff/vacation-requests";
    }

    @PreAuthorize("hasRole('MANAGER')")
    @GetMapping("/manger/vacation-requests")
    public String vacationRequestsManager(Model model) {

        model.addAttribute("vacationRequests", vacations.findAll());

        return "manager/vacationRequests";
    }

    @PreAuthorize("hasRole('MANAGER')")
    @GetMapping("/manager/vacation-request/{vacationRequest}")
    public String details(@PathVariable VacationRequest vacationRequest, Model model) {

        model.addAttribute("vacationRequest", vacationRequest);

        return "manager/vacationRequestDetails";
    }

    @PreAuthorize("hasRole('MANAGER')")
    @PostMapping("/manager/vacation-request/accept/{vacationRequest}")
    public String acceptRequest(@PathVariable VacationRequest vacationRequest) {

        if (vacationRequest.accept()) {
            vacations.save(vacationRequest);

        } else {
            throw new IllegalStateException(
                    "VacationRequest cannot be accepted! Current state: " + vacationRequest.getState());
        }
        return "redirect:/manger/vacation-requests";
    }

    @PreAuthorize("hasRole('MANAGER')")
    @PostMapping("/manager/vacation-request/decline/{vacationRequest}")
    public String declineRequest(@PathVariable VacationRequest vacationRequest) {

        if (vacationRequest.decline()) {
            vacations.save(vacationRequest);

        } else {
            throw new IllegalStateException(
                    "VacationRequest cannot be declined! Current state: " + vacationRequest.getState());
        }
        return "redirect:/manger/vacation-requests";
    }
}
