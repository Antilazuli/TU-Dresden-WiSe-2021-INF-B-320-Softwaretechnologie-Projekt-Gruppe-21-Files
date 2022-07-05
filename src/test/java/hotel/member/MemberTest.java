package hotel.member;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.salespointframework.useraccount.Password;
import org.salespointframework.useraccount.Role;
import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.UserAccountManagement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.util.Streamable;

import hotel.booking.BookingRepository;
import hotel.member.guest.Guest;
import hotel.member.manager.Manager;
import hotel.roster.RosterManagement;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class MemberTest {

	@Mock
	private MemberRepository memberRepository;

	@Mock
	private BookingRepository bookings;

	@Mock
	private UserAccountManagement userAccounts;

	@Mock
	private RosterManagement rosterManagement;

	@Autowired
	private MemberManagement memberManagement;

	@BeforeEach
	public void init() {

		memberManagement = new MemberManagement(memberRepository, userAccounts, rosterManagement);
	}

	@Test
	void testGUESTMemberCreation() {
		// Tworzenie obiektu typu mock (nie mamy jak stworzyc obiektu przez new)
		UserAccount account = mock(UserAccount.class);
		account.setFirstname("a");
		account.setLastname("b");
		account.setFirstname("c");
		// Kiedy wywolujemy metode tworrzenia nowego konta, zawsze zwracamny konto
		// zmockowane
		when(userAccounts.create(anyString(), any(Password.UnencryptedPassword.class), anyString()))
				.thenReturn(account);
		memberManagement.createGuest(RegistrationForm.of("a", "b", "mail@mail.com", "d"));
		// Przechwytujemy argument jaki przekazujemy do repository (aktualizujemy obiekt
		// typu member)
		ArgumentCaptor<Member> argument = ArgumentCaptor.forClass(Member.class);
		verify(memberRepository).save(argument.capture());
		// sprawdzamy, czy aktualizowany obiekt posiada role GUEST
		assertEquals("GUEST", argument.getValue().getRole().toString());
	}

	@Test
	void testMANAGERMemberCreation() {
		UserAccount account = mock(UserAccount.class);
		account.setFirstname("a");
		account.setLastname("b");
		account.setFirstname("c");
		when(userAccounts.create(anyString(), any(Password.UnencryptedPassword.class), anyString()))
				.thenReturn(account);
		when(memberRepository.save(any(Member.class))).thenReturn(mock(Manager.class));
		memberManagement.createManager(RegistrationForm.of("a", "b", "mail@mail.com", "d"));
		ArgumentCaptor<Member> argument = ArgumentCaptor.forClass(Member.class);
		verify(memberRepository).save(argument.capture());
		assertEquals("MANAGER", argument.getValue().getRole().toString());
	}

	@Test
	void testCheckIn() {
		UserAccount userAccount = mock(UserAccount.class);
		Guest guest = new Guest(userAccount);

		when(guest.getUserAccount().getRoles()).thenReturn(Streamable.of(new ArrayList<>()));
		memberManagement.checkInGuest(guest);
		ArgumentCaptor<Role> argument = ArgumentCaptor.forClass(Role.class);
		verify(userAccount, times(3)).add(argument.capture());

		assertTrue(argument.getAllValues().stream().anyMatch(role -> role.getName().equalsIgnoreCase("SERVICE")));
	}

	@Test
	void testCheckOut() {
		UserAccount userAccount = mock(UserAccount.class);
		Guest guest = new Guest(userAccount);

		when(guest.getUserAccount().getRoles()).thenReturn(Streamable.of(new ArrayList<>()));
		memberManagement.checkInGuest(guest);
		memberManagement.checkOutGuest(guest);
		ArgumentCaptor<Role> argument = ArgumentCaptor.forClass(Role.class);
		verify(userAccount, times(1)).remove(argument.capture());
		List<Role> allRoles = argument.getAllValues();
		assertTrue(allRoles.stream().anyMatch(role -> role.getName().equalsIgnoreCase("SERVICE")));
	}
}
