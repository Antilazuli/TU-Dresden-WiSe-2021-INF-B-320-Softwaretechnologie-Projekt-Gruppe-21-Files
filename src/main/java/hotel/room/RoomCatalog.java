package hotel.room;

import org.salespointframework.catalog.Catalog;
import org.springframework.data.domain.Sort;
import org.springframework.data.util.Streamable;

import hotel.room.Room.Type;

public interface RoomCatalog extends Catalog<Room> {

    static final Sort DEFAULT_SORT = Sort.by("productIdentifier").descending();

    Iterable<Room> findByType(Type type, Sort sort);

    default Iterable<Room> findByType(Type type) {
        return findByType(type, DEFAULT_SORT);
    }

    @Override
    public Streamable<Room> findAll();

    public default Streamable<Room> findAllNoCopy() {
        return findAll().filter(room -> !(room instanceof RoomCopy));
    }
}
