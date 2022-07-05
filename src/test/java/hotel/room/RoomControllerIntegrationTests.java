package hotel.room;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;

import hotel.AbstractIntegrationTests;
import hotel.room.controller.RoomController;

public class RoomControllerIntegrationTests extends AbstractIntegrationTests {

    @Autowired
    RoomController controller;

    @Autowired
    RoomCatalog rooms;

    @Test
    void setDates() {

        String returnedView = controller.setDates(new FilterForm("2022-02-20",
                "2022-02-27", List.of("SUITE"), null));

        assertEquals("redirect:/rooms", returnedView);
    }

    @Test
    void setDatesExceptionEquipment() {

        String returnedView = controller
                .setDates(new FilterForm("2022-03-20", "2022-03-27", null, List.of("test")));

        assertEquals("redirect:/rooms", returnedView);
    }

    @Test
    void sortRooms() {

        Model model = new ExtendedModelMap();

        String returnedView = controller.sortRooms("PRICE", model);

        assertEquals("redirect:/rooms", returnedView);

        // Sortierung umkehren
        returnedView = controller.sortRooms("PRICE", model);

        assertEquals("redirect:/rooms", returnedView);
    }

    @Test
    void sortRoomsException() {

        Model model = new ExtendedModelMap();

        String returnedView = controller.sortRooms("nothing", model);

        assertEquals("redirect:/rooms", returnedView);
    }

    @Test
    void rooms() {

        Model model = new ExtendedModelMap();

        controller.sortRooms("TYPE", model);
        String returnedView = controller.rooms(model);

        assertEquals("member/rooms", returnedView);

        // Sortierung umkehren
        controller.sortRooms("TYPE", model);
        returnedView = controller.rooms(model);

        assertEquals("member/rooms", returnedView);
    }

    @Test
    void roomsEmpty() {

        Model model = new ExtendedModelMap();

        controller.sortRooms("nothing", model);
        controller.rooms("all", model);
        String returnedView = controller.rooms(model);

        assertEquals("member/rooms", returnedView);
    }

    @Test
    void roomsType() {

        Model model = new ExtendedModelMap();

        String returnedView = controller.rooms("SUITE", model);

        assertEquals("redirect:/rooms", returnedView);
    }

    // @Test
    // void roomsTypeException() {

    // Model model = new ExtendedModelMap();

    // String returnedView = controller.rooms("all", model);

    // assertEquals("redirect:/rooms", returnedView);
    // }

    @Test
    void details() {

        Model model = new ExtendedModelMap();

        String returnedView = controller.details(rooms.findAll().iterator().next(), model);

        assertEquals("member/roomDetails", returnedView);
    }

    @Test
    void detailsSetDates() {

        Room room = rooms.findAll().iterator().next();

        String returnedView = controller.detailsSetDates(room, new PriceForm("EUR 70", "2022-06-15", "2022-07-15"));

        assertEquals("redirect:/room/" + room.getId(), returnedView);
    }
}
