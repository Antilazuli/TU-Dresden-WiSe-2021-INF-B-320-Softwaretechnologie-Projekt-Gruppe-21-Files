package hotel.member.guest;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class GuestEditFormUnitTests {

    @Test
    void guestEditForm() {

        GuestEditForm form = new GuestEditForm("testa", "testfrau", "12345678", "123456789", "123", "123");

        assertEquals("testa", form.getFirstname());
        assertEquals("testfrau", form.getLastname());
        assertEquals("12345678", form.getPhoneNumber());
        assertEquals("123456789", form.getCreditCardNumber());
        assertEquals("123", form.getNewPassword());
        assertEquals("123", form.getRepPassword());
    }
}
