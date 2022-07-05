package hotel.member;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class MemberFormUnitTests {

    @Test
    void memberForm() {

        MemberForm form = new MemberForm("Max", "Mustermann", "123");

        assertEquals("Max", form.getFirstname());
        assertEquals("Mustermann", form.getLastname());
        assertEquals("123", form.getNewPassword());
    }
}
