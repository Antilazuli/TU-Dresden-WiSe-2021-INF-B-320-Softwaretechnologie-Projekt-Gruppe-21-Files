package hotel.member.manager;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import javax.money.MonetaryAmount;

import org.javamoney.moneta.Money;
import org.salespointframework.core.Currencies;
import org.springframework.data.util.Streamable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;

import hotel.booking.Booking;
import hotel.booking.BookingRepository;
import hotel.room.Room;
import hotel.room.RoomCatalog;
import hotel.room.RoomInventory;

@PreAuthorize("hasRole('MANAGER')")
@Controller
public class ManagerController {

    private final BookingRepository bookings;
    private final RoomCatalog rooms;
    private final RoomInventory inventory;

    public ManagerController(BookingRepository bookings, RoomCatalog rooms, RoomInventory inventory) {

        Assert.notNull(bookings, "BookingRepository must not be null!");
        Assert.notNull(rooms, "RoomCatalog must not be null!");
        Assert.notNull(inventory, "RoomInventory must not be null!");

        this.bookings = bookings;
        this.rooms = rooms;
        this.inventory = inventory;
    }

    @GetMapping("/manager/finances")
    public String finances(Model model) {

        LocalDate yesterday = LocalDate.now().minusDays(1);

        Streamable<Booking> b = bookings.findAllByDate(yesterday);

        MonetaryAmount total = Money.of(0, Currencies.EURO);

        for (Booking booking : b) {

            total = total.add(booking.getTotal(yesterday));
        }

        model.addAttribute("date", yesterday);
        model.addAttribute("bookings", b);
        model.addAttribute("total", total);

        return "manager/finances";
    }

    @GetMapping("/manager/statistics")
    public String statistics(Model model) {

        Calendar calendar = Calendar.getInstance();
        int monthDayCount = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        LocalDate start = LocalDate.now().withDayOfMonth(1);
        LocalDate end = start.withDayOfMonth(monthDayCount);

        Map<Room, String> roomWithPercentage = new HashMap<>();
        Map<Room, Long> roomWithTotalCount = new HashMap<>();

        for (Room room : rooms.findAllNoCopy()) {

            Long monthRoomCount = inventory.findByRoomIdAndIsBookedBetweenDates(room.getId(), start, end).get()
                    .count();
            roomWithPercentage.put(room, Math.round((monthRoomCount * 1f) / (monthDayCount * 1f) * 100f) + " %");
            roomWithTotalCount.put(room, inventory.findByRoomIdAndIsBooked(room.getId()).get().count());
        }

        model.addAttribute("roomWithPercentage", roomWithPercentage);
        model.addAttribute("roomWithTotalCount", roomWithTotalCount);

        return "manager/statistics";
    }
}
