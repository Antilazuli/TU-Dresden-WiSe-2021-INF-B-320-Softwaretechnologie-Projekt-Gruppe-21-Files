package hotel.room;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.money.MonetaryAmount;

import org.javamoney.moneta.Money;

public class PriceForm {

    private final MonetaryAmount price;
    private final LocalDate start;
    private final LocalDate end;

    public PriceForm(String price, String startString, String endString) {

        this.price = Money.parse(price);
        this.start = LocalDate.parse(startString, DateTimeFormatter.ISO_DATE);
        this.end = LocalDate.parse(endString, DateTimeFormatter.ISO_DATE);
    }

    public MonetaryAmount getPrice() {
        return price;
    }

    public LocalDate getStart() {
        return start;
    }

    public LocalDate getEnd() {
        return end;
    }
}
