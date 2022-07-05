package hotel.room;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;

import org.javamoney.moneta.Money;
import org.junit.jupiter.api.Test;

public class PriceFormUnitTests {

    @Test
    void priceForm() {

        PriceForm form = new PriceForm("EUR 70", "2022-06-15", "2022-07-15");

        assertEquals(Money.parse("EUR 70"), form.getPrice());
        assertEquals(LocalDate.of(2022, 6, 15), form.getStart());
        assertEquals(LocalDate.of(2022, 7, 15), form.getEnd());
    }
}
