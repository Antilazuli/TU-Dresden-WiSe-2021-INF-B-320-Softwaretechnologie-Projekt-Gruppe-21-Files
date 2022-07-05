package hotel.member;

import java.util.List;

import org.salespointframework.core.DataInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Component
@Order(15)
class MemberDataInitializer implements DataInitializer {

	private static final Logger LOG = LoggerFactory.getLogger(MemberDataInitializer.class);

	private final MemberManagement memberManagement;

	MemberDataInitializer(MemberManagement memberManagement) {

		Assert.notNull(memberManagement, "MemberManagement must not be null!");

		this.memberManagement = memberManagement;
	}

	@Override
	public void initialize() {

		LOG.info("Creating default members.");

		var password = "123";

		List.of(RegistrationForm.of("Root", "Manager", "root@hotel.de", password),
				RegistrationForm.of("Nelli", "Rahn", "nelli@hotel.de", password))
				.forEach(memberManagement::createManager);

		List.of(RegistrationForm.of("Peter", "Lustig", "peter@hotel.de", password),
				RegistrationForm.of("Max", "Knecht", "max@hotel.de", password),
				RegistrationForm.of("Hans", "Knecht", "hans@hotel.de", password))
				.forEach(memberManagement::createStaff);

		List.of(RegistrationForm.of("Johannes", "Pforr", "johannes@hotel.de", password),
				RegistrationForm.of("Mika", "Tax", "mika@hotel.de", password),
				RegistrationForm.of("Eric", "Müller", "eric@hotel.de", password),
				RegistrationForm.of("Ralf", "Bachman", "ralf@hotel.de", password),
				RegistrationForm.of("Szymon", "Krupecki", "szymon@hotel.de", password),
				RegistrationForm.of("Hannes", "Richter", "hannes@hotel.de", password),
				RegistrationForm.of("User", "User", "user@hotel.de", password))
				.forEach(memberManagement::createGuest);
	}
}
