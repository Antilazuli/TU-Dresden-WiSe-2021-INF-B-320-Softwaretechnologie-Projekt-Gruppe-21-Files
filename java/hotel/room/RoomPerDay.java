package hotel.room;

import java.time.LocalDate;

import javax.money.MonetaryAmount;
import javax.persistence.Entity;

import org.javamoney.moneta.Money;
import org.salespointframework.inventory.MultiInventoryItem;
import org.salespointframework.quantity.Quantity;

@Entity
public class RoomPerDay extends MultiInventoryItem {

    private LocalDate date;

    private double percentage;

    @SuppressWarnings("unused")
    private RoomPerDay() {
    }

    public RoomPerDay(Room room, LocalDate date) {
        this(room, date, Quantity.of(1));
    }

    public RoomPerDay(Room room, LocalDate date, Quantity quantity) {

        super(room, quantity);
        this.date = date;

        percentage = 1;
    }

    public Room getRoom() {
        return (Room) super.getProduct();
    }

    public LocalDate getDate() {
        return date;
    }

    public MonetaryAmount getPrice() {

        MonetaryAmount price = getRoom().getPrice();

        return Money.of(price.getNumber().doubleValue() * percentage, price.getCurrency());
    }

    public void setPercentage(int percentage) {
        this.percentage = percentage / 100.0 + 1.0;
    }

    @Override
    public String toString() {
        return getRoom() + " - " + date + " - " + getQuantity();
    }
}
