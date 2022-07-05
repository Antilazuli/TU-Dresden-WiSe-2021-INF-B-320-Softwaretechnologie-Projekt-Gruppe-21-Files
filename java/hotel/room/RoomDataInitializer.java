package hotel.room;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.salespointframework.core.DataInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(10)
class RoomDataInitializer implements DataInitializer {

	private static final Logger LOG = LoggerFactory.getLogger(RoomDataInitializer.class);

	private final RoomCatalog catalog;

	RoomDataInitializer(RoomCatalog catalog) {
		this.catalog = catalog;
	}

	@Override
	public void initialize() {

		LOG.info("Creating default rooms.");

		List<Room> rooms = new ArrayList<>();

		for (int i = 0; i < 15; i++) {
			rooms.add(Room.getRandom(new Random(), i + 1));
		}

		rooms.forEach(catalog::save);
	}
}
