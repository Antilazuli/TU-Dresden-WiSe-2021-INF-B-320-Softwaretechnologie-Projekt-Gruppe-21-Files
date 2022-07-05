package hotel.room;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;

public class RoomAddFormUnitTests {

    @Test
    void roomAddForm() {

        RoomAddForm form = new RoomAddForm("testRaum", "100", "suite", List.of("equipment1", "equipment2"));

        assertEquals("testRaum", form.getName());
        assertEquals("100", form.getPrice());
        assertEquals("suite", form.getType());
        assertEquals(List.of("equipment1", "equipment2"), form.getEquipments());
    }

    @Test
    void roomAddFormEmpty() {

        RoomAddForm form = new RoomAddForm("testRaum", "100", "suite", null);

        assertEquals("testRaum", form.getName());
        assertEquals("100", form.getPrice());
        assertEquals("suite", form.getType());
        assertEquals(List.of(), form.getEquipments());
    }
}
