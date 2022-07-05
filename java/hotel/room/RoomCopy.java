package hotel.room;

import java.util.Set;

import javax.money.MonetaryAmount;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import org.salespointframework.catalog.ProductIdentifier;

@Entity
public class RoomCopy extends Room {

    @ManyToOne
    private Room room;

    @SuppressWarnings("unused")
    private RoomCopy() {
    }

    public RoomCopy(Room room, MonetaryAmount price) {

        super(room.getName(), price, room.getType(), Set.copyOf(room.getEquipments()));

        this.room = room;
    }

    public Room getRoom() {
        return room;
    }

    public ProductIdentifier getRefId() {
        return room.getId();
    }

    @Override
    public String toString() {
        return super.toString() + " (copy)";
    }
}
