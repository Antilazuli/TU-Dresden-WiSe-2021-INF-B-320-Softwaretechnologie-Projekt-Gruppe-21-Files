package hotel.room;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;

import hotel.AbstractIntegrationTests;
import hotel.room.controller.RoomManagerController;

public class RoomManagerControllerIntegrationTests extends AbstractIntegrationTests {

    @Autowired
    RoomManagerController controller;

    @Autowired
    RoomCatalog rooms;

    @Test
    @WithMockUser(roles = "MANAGER")
    void rooms() {

        Model model = new ExtendedModelMap();

        String returnedView = controller.rooms(model);

        assertEquals("manager/rooms", returnedView);
    }

    @Test
    @WithMockUser(roles = "MANAGER")
    void roomDetail() {

        Model model = new ExtendedModelMap();

        String returnedView = controller.roomDetail(rooms.findAll().iterator().next(), model);

        assertEquals("manager/roomDetails", returnedView);
    }

    @Test
    @WithMockUser(roles = "MANAGER")
    void addRoomGet() {

        Model model = new ExtendedModelMap();

        String returnedView = controller.addRoom(model, mock(RoomAddForm.class));

        assertEquals("manager/addRoom", returnedView);
    }

    @Test
    @WithMockUser(roles = "MANAGER")
    void addRoomPost() {

        String returnedView = controller.addRoom(new RoomAddForm("test", "100", "SUITE", null));

        assertEquals("redirect:/manager/rooms", returnedView);
    }

    @Test
    @WithMockUser(roles = "MANAGER")
    void editRoomGet() {

        Model model = new ExtendedModelMap();

        String returnedView = controller.editRoom(rooms.findAll().iterator().next(), mock(RoomAddForm.class), model);

        assertEquals("manager/editRoom", returnedView);
    }

    @Test
    @WithMockUser(roles = "MANAGER")
    void editRoomPost() {

        String returnedView = controller.editRoom(rooms.findAll().iterator().next(),
                new RoomAddForm("test", "100", "SUITE", null));

        assertEquals("redirect:/manager/rooms", returnedView);
    }
}
