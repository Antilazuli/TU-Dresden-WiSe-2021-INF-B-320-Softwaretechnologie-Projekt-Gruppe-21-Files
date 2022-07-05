package hotel.member.guest;

import java.util.Optional;

import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.web.LoggedIn;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import hotel.member.MemberManagement;

@PreAuthorize("hasRole('GUEST')")
@Controller
public class GuestController {

	private final MemberManagement management;

	public GuestController(MemberManagement management) {

		Assert.notNull(management, "MemberManagement must not be null!");

		this.management = management;
	}

	@GetMapping("/guest")
	public String guest(Model model, @LoggedIn Optional<UserAccount> userAccount) {

		if (userAccount.isPresent()) {
			model.addAttribute("guest", management.findByUserAccount(userAccount.get()));
		}

		return "guest/guest";
	}

	@PostMapping("/updateGuest")
	public String updateGuestDone(Model model, @LoggedIn Optional<UserAccount> userAccount, GuestEditForm form) {

		if (userAccount.isPresent()) {

			Guest guest = (Guest) management.findByUserAccount(userAccount.get());

			management.updateGuest(guest, form);
		}
		return "redirect:/guest";
	}

	@GetMapping("/guest/update")
	public String updateGuestNew(Model model, @LoggedIn Optional<UserAccount> userAccount, GuestEditForm form) {

		if (userAccount.isPresent()) {

			Guest guest = (Guest) management.findByUserAccount(userAccount.get());

			model.addAttribute("guest", guest);
		}

		return "guest/guestUpdate";
	}

	@PreAuthorize("hasRole('MANAGER')")
	@GetMapping("/manager/guests")
	public String guests(Model model) {

		model.addAttribute("guests", management.findAllGuests());

		return "manager/guests";
	}
}
