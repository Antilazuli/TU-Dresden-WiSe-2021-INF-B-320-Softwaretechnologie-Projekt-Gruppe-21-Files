package hotel.member.manager;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;

import hotel.AbstractIntegrationTests;
import hotel.booking.BookingRepository;
import hotel.room.RoomCatalog;

public class ManagerControllerIntegrationTests extends AbstractIntegrationTests {

    @Autowired
    ManagerController controller;

    @Autowired
    BookingRepository bookings;

    @Autowired
    RoomCatalog rooms;

    @Test
    @WithMockUser(roles = "MANAGER")
    void finances() {

        Model model = new ExtendedModelMap();

        String returnedView = controller.finances(model);

        assertEquals("manager/finances", returnedView);
    }

    @Test
    @WithMockUser(roles = "MANAGER")
    void statistics() {

        Model model = new ExtendedModelMap();

        String returnedView = controller.statistics(model);

        assertEquals("manager/statistics", returnedView);
    }
}
