package hotel.member;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;

import hotel.AbstractIntegrationTests;
import hotel.member.guest.Guest;

class MemberControllerIntegrationTests extends AbstractIntegrationTests {

	@Autowired
	MemberController controller;

	@Autowired
	MemberRepository members;

	@Test
	void indexEmpty() {

		Model model = new ExtendedModelMap();

		String returnedView = controller.index(model, Optional.empty());

		assertEquals("index", returnedView);
	}

	@Test
	void index() {

		Model model = new ExtendedModelMap();

		Guest guest = (Guest) members.findByEmail("mika@hotel.de").get();

		String returnedView = controller.index(model, Optional.of(guest.getUserAccount()));

		assertEquals("index", returnedView);
	}

	@Test
	void register() {

		Model model = new ExtendedModelMap();

		String returnedView = controller.register(model, mock(RegistrationForm.class));

		assertEquals("member/register", returnedView);
	}

	@Test
	void registerGuest() {

		String returnedView = controller.registerGuest(RegistrationForm.of("Max", "Mustermann", "test@hotel.de", "123"),
				mock(Errors.class));

		assertEquals("redirect:/login", returnedView);
	}

	@Test
	void registerGuestError() {

		Errors errors = mock(Errors.class);
		when(errors.hasErrors()).thenReturn(true);

		String returnedView = controller.registerGuest(mock(RegistrationForm.class), errors);

		assertEquals("member/register", returnedView);
	}

	@Test
	void registerGuestMailError() {

		Errors errors = mock(Errors.class);
		when(errors.hasErrors()).thenReturn(true);

		String returnedView = controller.registerGuest(RegistrationForm.of("Max", "Mustermann", "max@hotel.de", "123"),
				errors);

		assertEquals("member/register", returnedView);
	}
}
