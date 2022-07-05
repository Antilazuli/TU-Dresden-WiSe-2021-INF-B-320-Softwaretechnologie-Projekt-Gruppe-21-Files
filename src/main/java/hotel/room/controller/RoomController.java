package hotel.room.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import javax.money.MonetaryAmount;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import hotel.room.FilterForm;
import hotel.room.PriceForm;
import hotel.room.Room;
import hotel.room.Room.Equipment;
import hotel.room.Room.SortOption;
import hotel.room.Room.Type;
import hotel.room.RoomManagement;

@Controller
public class RoomController {

    private static final String ROOMS_ROUTE = "redirect:/rooms";

    private final RoomManagement management;

    private LocalDate start;
    private LocalDate end;

    private EnumMap<Equipment, Boolean> equipmentMap;
    private List<Type> types;
    private List<Equipment> equipments;

    private EnumMap<SortOption, Boolean> sortOptionMap;
    private SortOption sortOption;

    private MonetaryAmount price;

    public RoomController(RoomManagement management) {

        this.management = management;

        start = LocalDate.now();
        end = start.plusDays(1);

        types = new ArrayList<>();
        equipments = new ArrayList<>();
        equipmentMap = new EnumMap<>(Equipment.class);

        for (Equipment equipment : Equipment.values()) {
            equipmentMap.put(equipment, false);
        }

        sortOptionMap = new EnumMap<>(SortOption.class);
    }

    @PostMapping("/setDates")
    public String setDates(FilterForm form) {

        start = LocalDate.parse(form.getStart(), DateTimeFormatter.ISO_DATE);
        end = LocalDate.parse(form.getEnd(), DateTimeFormatter.ISO_DATE);

        try {
            types = form.getTypes().stream().map(Type::valueOf).collect(Collectors.toList());
        } catch (Exception e) {
            types.clear();
        }

        try {
            equipments = form.getEquipments().stream().map(Equipment::valueOf).collect(Collectors.toList());
        } catch (Exception e) {
            equipments.clear();
        }

        for (Equipment equipment : Equipment.values()) {
            equipmentMap.put(equipment, equipments.contains(equipment));
        }

        return ROOMS_ROUTE;
    }

    @GetMapping("/rooms/sort/{sortOption}")
    public String sortRooms(@PathVariable String sortOption, Model model) {

        try {
            this.sortOption = SortOption.valueOf(sortOption.toUpperCase());

            if (sortOptionMap.containsKey(this.sortOption)) {
                sortOptionMap.put(this.sortOption, !sortOptionMap.get(this.sortOption));
            } else {
                sortOptionMap.put(this.sortOption, false);
            }
        } catch (Exception e) {
            this.sortOption = null;
        }

        return ROOMS_ROUTE;
    }

    @GetMapping("/rooms")
    public String rooms(Model model) {

        var map = management.findAllAvailableRoomsByFilter(start, end.minusDays(1), types, equipments);

        Map<Room, MonetaryAmount> sorted = new TreeMap<>(map);

        if (sortOption != null) {
            var comparator = sortOption.getComparator();
            if (sortOptionMap.getOrDefault(sortOption, false).booleanValue()) {
                comparator = comparator.reversed();
            }
            sorted = new TreeMap<>(comparator);
            sorted.putAll(map);
        }

        model.addAttribute("rooms", sorted);

        model.addAttribute("start", start);
        model.addAttribute("end", end);
        if (!types.isEmpty()) {
            model.addAttribute("selectedType", types.iterator().next());
        }
        model.addAttribute("types", Room.Type.values());
        model.addAttribute("equipments", equipmentMap);

        return "member/rooms";
    }

    @GetMapping("/rooms/{type}")
    public String rooms(@PathVariable String type, Model model) {

        try {
            types = List.of(Type.valueOf(type.toUpperCase()));
        } catch (Exception e) {
            types.clear();
        }

        return ROOMS_ROUTE;
    }

    @GetMapping("/room/{room}")
    public String details(@PathVariable Room room, Model model) {

        model.addAttribute("room", room);
        model.addAttribute("price", price);
        model.addAttribute("start", start);
        model.addAttribute("end", end);

        return "member/roomDetails";
    }

    @PostMapping("/room/{room}")
    public String detailsSetDates(@PathVariable Room room, PriceForm form) {

        price = form.getPrice();
        start = form.getStart();
        end = form.getEnd();

        return "redirect:/room/" + room.getId();
    }
}
