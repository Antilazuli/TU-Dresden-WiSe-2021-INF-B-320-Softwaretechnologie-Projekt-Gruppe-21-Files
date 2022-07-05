package hotel.member;

import java.util.Optional;

import javax.validation.Valid;

import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.web.LoggedIn;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class MemberController {

    private final MemberManagement memberManagement;

    public MemberController(MemberManagement memberManagement) {

        Assert.notNull(memberManagement, "MemberManagement must not be null!");

        this.memberManagement = memberManagement;
    }

    @GetMapping("/")
    public String index(Model model, @LoggedIn Optional<UserAccount> userAccount) {

        if (userAccount.isPresent()) {
            model.addAttribute("member", memberManagement.findByUserAccount(userAccount.get()));
        }

        return "index";
    }

    @PostMapping("/register")
    public String registerGuest(@Valid RegistrationForm form, Errors result) {

        if (memberManagement.findByEmail(form.getEmail()).isPresent()) {
            result.rejectValue("email", "0", "Email already taken!");
            
        }

        if (result.hasErrors()) {
            System.out.println(result.getAllErrors());
            return "member/register";
        }

        memberManagement.createGuest(form);

        return "redirect:/login";
    }

    @GetMapping("/register")
    public String register(Model model, RegistrationForm form) {
        return "member/register";
    }
}
