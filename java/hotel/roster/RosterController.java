package hotel.roster;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.web.LoggedIn;
import org.springframework.data.util.Streamable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;

import hotel.member.MemberManagement;
import hotel.member.staff.Staff;

@Controller
public class RosterController {

    private final MemberManagement memberManagement;
    private final RosterManagement rosterManagement;
    private final WorkingDayRepository workingDays;

    public RosterController(MemberManagement memberManagement, RosterManagement rosterManagement,
            WorkingDayRepository workingDays) {

        Assert.notNull(memberManagement, "MemberManagement must not be null!");
        Assert.notNull(rosterManagement, "RosterManagement must not be null!");
        Assert.notNull(workingDays, "WorkingDays must not be null!");

        this.memberManagement = memberManagement;
        this.rosterManagement = rosterManagement;
        this.workingDays = workingDays;
    }

    @PreAuthorize("hasRole('STAFF')")
    @GetMapping("/staff/roster")
    public String rosterStaff(Model model, @LoggedIn Optional<UserAccount> userAccount) {

        if (userAccount.isPresent()) {
            Staff staff = (Staff) memberManagement.findByUserAccount(userAccount.get());

            LocalDate start = LocalDate.now().with(DayOfWeek.MONDAY);
            LocalDate end = LocalDate.now().with(DayOfWeek.SUNDAY);

            String[] days = start.datesUntil(end.plusDays(1))
                    .flatMap(date -> Stream.of(DayOfWeek.from(date).getDisplayName(TextStyle.FULL, Locale.GERMAN)))
                    .toArray(String[]::new);

            rosterManagement.createWorkingDays(start.datesUntil(end.plusDays(1)).toArray(LocalDate[]::new));

            model.addAttribute("days", days);
            model.addAttribute("workingDays", workingDays.findByIdBetweenDates(staff.getId(), start, end));
        }
        return "staff/roster";
    }

    @PreAuthorize("hasRole('MANAGER')")
    @GetMapping("/manager/roster")
    public String rosterManager(Model model) {

        LocalDate start = LocalDate.now().with(DayOfWeek.MONDAY);
        LocalDate end = LocalDate.now().with(DayOfWeek.SUNDAY);

        String[] days = start.datesUntil(end.plusDays(1))
                .flatMap(date -> Stream.of(DayOfWeek.from(date).getDisplayName(TextStyle.FULL, Locale.GERMAN)))
                .toArray(String[]::new);

        rosterManagement.createWorkingDays(start.datesUntil(end.plusDays(1)).toArray(LocalDate[]::new));

        Map<Staff, Streamable<WorkingDay>> dayPerSatff = new HashMap<>();
        for (Staff staff : memberManagement.findAllStaffs()) {
            dayPerSatff.put(staff, workingDays.findByIdBetweenDates(staff.getId(), start, end));
        }

        model.addAttribute("days", days);
        model.addAttribute("dayPerSatff", dayPerSatff);

        return "manager/roster";
    }
}
