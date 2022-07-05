package hotel.room;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;

import javax.money.MonetaryAmount;

import org.salespointframework.quantity.Quantity;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Service;

import hotel.room.Room.Equipment;
import hotel.room.Room.Type;

@Service
public class RoomManagement {

    private final RoomInventory inventory;
    private final RoomCatalog catalog;

    public RoomManagement(RoomInventory inventory, RoomCatalog catalog) {

        this.inventory = inventory;
        this.catalog = catalog;
    }

    public void updateStock(LocalDate start, LocalDate end) {

        catalog.findAllNoCopy().forEach(room -> start.datesUntil(end).forEach(date -> {
            if (inventory.findByIdAtDate(room.getId(), date).isEmpty()) {
                inventory.save(new RoomPerDay(room, date));
            }
        }));
    }

    public void decreaseStock(Room room, LocalDate start, LocalDate end) {

        start.datesUntil(end).forEach(date -> inventory.save(inventory.findByIdAtDate(room.getId(), date)
                .map(it -> (RoomPerDay) it.decreaseQuantity(Quantity.of(1)))
                .orElse(new RoomPerDay(room, date, Quantity.of(0)))));
    }

    public void increaseStock(Room room, LocalDate start, LocalDate end) {

        start.datesUntil(end).forEach(date -> inventory.save(inventory.findByIdAtDate(room.getId(), date)
                .map(it -> (RoomPerDay) it.increaseQuantity(Quantity.of(1)))
                .orElse(new RoomPerDay(room, date))));
    }

    public void increaseStock(Set<Room> rooms, LocalDate start, LocalDate end) {

        rooms.forEach(room -> increaseStock(room, start, end));
    }

    public void updatePrice(LocalDate start, LocalDate end, int percentage) {

        updateStock(start, end);

        inventory.findBetweenDate(start, end).forEach(day -> {
            day.setPercentage(percentage);
            inventory.save(day);
        });
    }

    // Map: alle Räume mit max Preis
    public Map<Room, MonetaryAmount> findAllAvailableRooms(LocalDate start, LocalDate end) {

        updateStock(start, end);

        Map<Room, MonetaryAmount> map = new TreeMap<>();

        // Anzahl der Tage
        long dateCount = start.datesUntil(end.plusDays(1)).count();

        // für jeden Raum im Catalog
        for (Room room : catalog.findAllNoCopy()) {

            Streamable<RoomPerDay> days = inventory.findAvaibleByIdBetweenDates(room.getId(), start, end);

            // Raum durchgängig (an allen Tagen) verfügbar
            if (days.get().count() == dateCount) {
                // finde Tag mit höchstem Preis
                RoomPerDay day = days.stream().max(Comparator.comparing(RoomPerDay::getPrice))
                        .orElseThrow(NoSuchElementException::new);
                map.put(day.getRoom(), day.getPrice());
            }
        }

        return map;
    }

    public Map<Room, MonetaryAmount> findAllAvailableRoomsByFilter(LocalDate start, LocalDate end, List<Type> types,
            List<Equipment> equipments) {

        var map = findAllAvailableRooms(start, end);

        if (!types.isEmpty()) {
            map = map.entrySet().stream().filter(r -> types.contains(r.getKey().getType()))
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        }

        if (!equipments.isEmpty()) {
            map = map.entrySet().stream().filter(r -> r.getKey().getEquipments().containsAll(equipments))
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        }

        return map;
    }
}
