package hotel.member;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.salespointframework.useraccount.Password.UnencryptedPassword;
import org.salespointframework.useraccount.Role;
import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.UserAccountManagement;
import org.springframework.data.util.Streamable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import hotel.member.guest.Guest;
import hotel.member.guest.GuestEditForm;
import hotel.member.manager.Manager;
import hotel.member.staff.Staff;
import hotel.roster.RosterManagement;

@Service
public class MemberManagement {

	public static final Role MANAGER_ROLE = Role.of("MANAGER");
	public static final Role STAFF_ROLE = Role.of("STAFF");
	public static final Role GUEST_ROLE = Role.of("GUEST");
	public static final Role SERVICE_ROLE = Role.of("SERVICE");
	public static final Role RATING_ROLE = Role.of("RATING");

	public static final String GUEST_ROUTE = "guest/";

	private final MemberRepository members;
	private final UserAccountManagement accountManagement;
	private final RosterManagement rosterManagement;

	public MemberManagement(MemberRepository members, UserAccountManagement accountManagement,
			RosterManagement rosterManagement) {

		Assert.notNull(members, "MemberRepository must not be null!");
		Assert.notNull(accountManagement, "UserAccountManagement must not be null!");
		Assert.notNull(rosterManagement, "RosterManagement must not be null!");

		this.members = members;
		this.accountManagement = accountManagement;
		this.rosterManagement = rosterManagement;
	}

	private UserAccount toUserAccount(RegistrationForm form) {
		var email = form.getEmail();
		var username = email;
		var password = UnencryptedPassword.of(form.getPassword());
		var userAccount = accountManagement.create(username, password, email);
		userAccount.setFirstname(form.getFirstname());
		userAccount.setLastname(form.getLastname());
		return userAccount;
	}

	private void assertForm(RegistrationForm form) {
		Assert.notNull(form, "Registration form must not be null!");
	}

	/**
	 * Creates a new {@link Guest} using the information given in the
	 * {@link RegistrationForm}.
	 *
	 * @param form must not be {@literal null}.
	 * @return the new {@link Guest} instance.
	 */
	public Guest createGuest(RegistrationForm form) {

		assertForm(form);

		return members.save(new Guest(toUserAccount(form), form.getPhoneNumber(), form.getCreditCardNumber()));
	}

	/**
	 * Creates a new {@link Manager} using the information given in the
	 * {@link RegistrationForm}.
	 *
	 * @param form must not be {@literal null}.
	 * @return the new {@link Manager} instance.
	 */
	public Manager createManager(RegistrationForm form) {

		assertForm(form);

		return members.save(new Manager(toUserAccount(form)));
	}

	/**
	 * Creates a new {@link Staff} using the information given in the
	 * {@link RegistrationForm}.
	 *
	 * @param form must not be {@literal null}.
	 * @return the new {@link Staff} instance.
	 */
	public Staff createStaff(RegistrationForm form) {

		assertForm(form);

		return members.save(new Staff(toUserAccount(form)));
	}

	public void deleteMember(Member member) {

		Assert.notNull(member, "Member must not be null!");

		if (member instanceof Staff) {
			rosterManagement.deleteAllWorkingDays((Staff) member);
		}

		members.delete(member);
		accountManagement.delete(member.getUserAccount());
		System.out.println(member + " enternt.");

	}

	public Streamable<Guest> findAllGuests() {
		return members.findAllGuests();
	}

	public Streamable<Staff> findAllStaffs() {
		return members.findAllStaffs();
	}

	public Optional<Member> findById(Long id) {
		return members.findById(id);
	}

	public Optional<Member> findByEmail(String email) {
		return members.findByEmail(email);
	}

	public Member findByUserAccount(UserAccount userAccount) {
		return members.findByUserAccount(userAccount);
	}

	private void reLogin(Guest guest) {
		// Authentification der Form: "ROLE_GUEST"
		Set<GrantedAuthority> authorities = new HashSet<>();
		for (Role role : guest.getUserAccount().getRoles()) {
			authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getName()));
		}
		Authentication reAuth = new UsernamePasswordAuthenticationToken(guest.getUserAccount().getUsername(),
				guest.getUserAccount().getPassword(), authorities);
		SecurityContextHolder.getContext().setAuthentication(reAuth);
	}

	public void checkInGuest(Guest guest) {

		guest.getUserAccount().add(SERVICE_ROLE);
		// Bewertung des Hotels beim ersten Besuch freigeschaltet, nie wieder entfernt
		guest.getUserAccount().add(RATING_ROLE);
		accountManagement.save(guest.getUserAccount());
		reLogin(guest);
	}

	public void checkOutGuest(Guest guest) {

		guest.getUserAccount().remove(SERVICE_ROLE);
		// Für Initialiserung einer CheckedOut Buchung :)
		guest.getUserAccount().add(RATING_ROLE);
		accountManagement.save(guest.getUserAccount());
		reLogin(guest);
	}

	public boolean passwordsAreIncorrect(String newPassword, String repeatedPassword) {
		return !newPassword.equalsIgnoreCase(repeatedPassword);
	}

	public void updateGuest(Guest guest, GuestEditForm form) {

		String firstname = form.getFirstname();
		String lastname = form.getLastname();
		String phoneNumber = form.getPhoneNumber();
		String creditCardNumber = form.getCreditCardNumber();
		String newPassword = form.getNewPassword();
		String repPassword = form.getRepPassword();

		if (!firstname.isBlank()) {
			guest.setFirstname(firstname);
		}

		if (!lastname.isBlank()) {
			guest.setLastname(lastname);
		}

		if (!phoneNumber.isBlank()) {
			guest.setPhoneNumber(phoneNumber);
		}

		if (!creditCardNumber.isBlank()) {
			guest.setCreditCardNumber(creditCardNumber);
		}

		if (!newPassword.isBlank() && newPassword.equals(repPassword)) {
			accountManagement.changePassword(guest.getUserAccount(), UnencryptedPassword.of(newPassword));
		}

		members.save(guest);
	}

	public void updateMember(Member member, MemberForm form) {

		String firstname = form.getFirstname();
		String lastname = form.getLastname();
		String newPassword = form.getNewPassword();

		if (!firstname.isBlank()) {
			member.setFirstname(firstname);
		}
		if (!lastname.isBlank()) {
			member.setLastname(lastname);
		}
		if (!newPassword.isBlank()) {
			accountManagement.changePassword(member.getUserAccount(), UnencryptedPassword.of(newPassword));
		}

		members.save(member);
	}
}
