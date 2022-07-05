package hotel.room;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;

public class FilterFormUnitTests {

    @Test
    void filterForm() {

        FilterForm form = new FilterForm("testStart", "testEnd", List.of("type1", "type2"),
                List.of("equipment1", "equipment2"));

        assertEquals("testStart", form.getStart());
        assertEquals("testEnd", form.getEnd());
        assertEquals(List.of("type1", "type2"), form.getTypes());
        assertEquals(List.of("equipment1", "equipment2"), form.getEquipments());
    }

    @Test
    void filterFormEmpty() {

        FilterForm form = new FilterForm("testStart", "testEnd", null, null);

        assertEquals("testStart", form.getStart());
        assertEquals("testEnd", form.getEnd());
        assertEquals(List.of(), form.getTypes());
        assertEquals(List.of(), form.getEquipments());
    }
}
