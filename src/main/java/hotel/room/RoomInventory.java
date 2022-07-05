package hotel.room;

import java.time.LocalDate;
import java.util.Optional;

import org.salespointframework.catalog.ProductIdentifier;
import org.salespointframework.inventory.MultiInventory;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.util.Streamable;

public interface RoomInventory extends MultiInventory<RoomPerDay> {

    @Query("select rpd from RoomPerDay rpd where rpd.product.productIdentifier = :roomId")
    public Streamable<RoomPerDay> findAllByRoomId(ProductIdentifier roomId);

    @Query("select rpd from RoomPerDay rpd where rpd.product.productIdentifier = :roomId and rpd.quantity.amount <= 0")
    public Streamable<RoomPerDay> findByRoomIdAndIsBooked(ProductIdentifier roomId);

    @Query(("select rpd from RoomPerDay rpd where rpd.product.productIdentifier = :roomId" +
            " and rpd.quantity.amount <= 0 and rpd.date between :start and :end"))
    public Streamable<RoomPerDay> findByRoomIdAndIsBookedBetweenDates(ProductIdentifier roomId, LocalDate start,
            LocalDate end);

    public Streamable<RoomPerDay> findByDate(LocalDate date);

    @Query("select rpd from RoomPerDay rpd where rpd.product.productIdentifier = :id and rpd.date = :date")
    public Optional<RoomPerDay> findByIdAtDate(ProductIdentifier id, LocalDate date);

    public default boolean isAvaibleBetweenDate(Room room, LocalDate start, LocalDate end) {
        for (RoomPerDay roomPerDay : findByProductBetweenDate(room.getId(), start, end).toSet()) {
            if (roomPerDay.getQuantity().isZeroOrNegative()) {
                return false;
            }
        }
        return true;
    }

    @Query("select rpd from RoomPerDay rpd where rpd.quantity.amount > 0 and rpd.date between :start and :end")
    public Streamable<RoomPerDay> findAvaibleDaysBetweenDates(LocalDate start, LocalDate end);

    @Query("select rpd from RoomPerDay rpd where rpd.product.productIdentifier = :id" +
            " and rpd.quantity.amount > 0 and rpd.date between :start and :end")
    public Streamable<RoomPerDay> findAvaibleByIdBetweenDates(ProductIdentifier id, LocalDate start, LocalDate end);

    @Query(("select rpd from RoomPerDay rpd where rpd.product.productIdentifier = :room" +
            " and rpd.date between :start and :end"))
    public Streamable<RoomPerDay> findByProductBetweenDate(ProductIdentifier room, LocalDate start, LocalDate end);

    @Query("select rpd from RoomPerDay rpd where rpd.date between :start and :end")
    public Streamable<RoomPerDay> findBetweenDate(LocalDate start, LocalDate end);

    @Query("select rpd from RoomPerDay rpd where rpd.quantity.amount >= 0 and rpd.date between :start and :end")
    public Streamable<RoomPerDay> findAllBooked(LocalDate start, LocalDate end);
}
