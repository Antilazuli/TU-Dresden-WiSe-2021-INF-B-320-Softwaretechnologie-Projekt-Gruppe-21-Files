package hotel.member.staff;

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

import hotel.member.Member;
import hotel.member.MemberForm;
import hotel.member.MemberManagement;
import hotel.member.RegistrationForm;

@PreAuthorize("hasRole('MANAGER')")
@Controller
public class StaffController {

    private final MemberManagement management;

    public StaffController(MemberManagement management) {

        Assert.notNull(management, "MemberManagement must not be null!");

        this.management = management;
    }

    @PreAuthorize("hasRole('STAFF')")
    @GetMapping("/staff")
    public String staff(Model model, @LoggedIn Optional<UserAccount> userAccount) {

        if (userAccount.isPresent()) {
            model.addAttribute("staff", management.findByUserAccount(userAccount.get()));
        }

        return "staff/staff";
    }

    @PreAuthorize("hasRole('STAFF')")
    @PostMapping("/updateStaff")
    public String updateStaff(Model model, @LoggedIn Optional<UserAccount> userAccount, MemberForm form) {

        if (userAccount.isPresent()) {
            management.updateMember(management.findByUserAccount(userAccount.get()), form);
        }

        return "redirect:/";
    }

    @GetMapping("/manager/staffs")
    public String staffs(Model model) {

        model.addAttribute("staffs", management.findAllStaffs());

        return "manager/staffs";
    }

    @GetMapping("/manager/staff/add")
    public String addStaff(RegistrationForm form, Model model) {
        return "manager/addStaff";
    }

    @PostMapping("/manager/staff/add")
    public String addStaff(RegistrationForm form) {

        management.createStaff(form);

        return "redirect:/manager/staffs";
    }

    @PostMapping("/removeStaff/{id}")
    public String removeStaff(@PathVariable String id, Model model) {

        var opt = management.findById(Long.parseLong(id));

        if (opt.isPresent()) {

            Member member = opt.get();

            if (member instanceof Staff) {
                management.deleteMember(member);
            }
        }
        return "redirect:/manager/staffs";
    }
}
