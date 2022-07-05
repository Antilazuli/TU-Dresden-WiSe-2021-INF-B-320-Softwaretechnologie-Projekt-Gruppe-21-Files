package hotel.room.controller;

import java.util.EnumMap;
import java.util.Set;
import java.util.stream.Collectors;

import javax.money.MonetaryAmount;

import org.javamoney.moneta.Money;
import org.salespointframework.core.Currencies;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import hotel.room.Room;
import hotel.room.Room.Equipment;
import hotel.room.Room.Type;
import hotel.room.RoomAddForm;
import hotel.room.RoomCatalog;

@PreAuthorize("hasRole('MANAGER')")
@Controller
public class RoomManagerController {

    private final RoomCatalog rooms;

    public RoomManagerController(RoomCatalog rooms) {

        Assert.notNull(rooms, "Rooms must not be null!");

        this.rooms = rooms;
    }

    @GetMapping("/manager/rooms")
    public String rooms(Model model) {

        model.addAttribute("rooms", rooms.findAllNoCopy());

        return "manager/rooms";
    }

    @GetMapping("/manager/room/{room}")
    public String roomDetail(@PathVariable Room room, Model model) {

        model.addAttribute("room", room);

        return "manager/roomDetails";
    }

    @PostMapping("/manager/room/add")
    public String addRoom(RoomAddForm form) {

        String name = form.getName();

        MonetaryAmount price = Money.of(Long.parseLong(form.getPrice()), Currencies.EURO);

        Type type = Type.valueOf(form.getType());

        Set<Equipment> equipments = form.getEquipments().stream().map(Equipment::valueOf)
                .collect(Collectors.toSet());

        rooms.save(new Room(name, price, type, equipments));

        return "redirect:/manager/rooms";
    }

    @GetMapping("/manager/room/add")
    public String addRoom(Model model, RoomAddForm form) {

        model.addAttribute("types", Type.values());
        model.addAttribute("equipments", Equipment.values());

        return "manager/addRoom";
    }

    @PostMapping("/manager/room/edit/{room}")
    public String editRoom(@PathVariable Room room, RoomAddForm form) {

        MonetaryAmount price = Money.of(Long.parseLong(form.getPrice()), Currencies.EURO);

        Set<Equipment> equipments = form.getEquipments().stream().map(Equipment::valueOf)
                .collect(Collectors.toSet());

        room.setPrice(price);
        room.setEquipments(equipments);

        rooms.save(room);

        return "redirect:/manager/rooms";
    }

    @GetMapping("/manager/room/edit/{room}")
    public String editRoom(@PathVariable Room room, RoomAddForm form, Model model) {

        model.addAttribute("name", room.getName());
        model.addAttribute("price", room.getPrice().getNumber());

        EnumMap<Equipment, Boolean> equipmentMap = new EnumMap<>(Equipment.class);
        for (Equipment equipment : Equipment.values()) {
            equipmentMap.put(equipment, room.getEquipments().contains(equipment));
        }

        model.addAttribute("equipments", equipmentMap);

        return "manager/editRoom";
    }
}
