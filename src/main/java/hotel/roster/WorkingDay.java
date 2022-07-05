package hotel.roster;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import hotel.member.staff.Staff;
import hotel.room.Room;

@Entity(name = "WorkingDay")
public class WorkingDay {

    private @Id @GeneratedValue long id;
    private LocalDate date;
    private LocalTime start;
    private LocalTime end;
    @ManyToOne
    private Staff staff;
    @ManyToMany
    protected Set<Room> rooms;

    protected WorkingDay() {
    }

    public WorkingDay(LocalDate date, Staff staff) {
        this.date = date;
        this.staff = staff;
        // Arbeitstag von 10 bis 18 Uhr
        start = LocalTime.of(10, 0);
        end = LocalTime.of(18, 0);
        rooms = new HashSet<>();
    }

    public long getId() {
        return id;
    }

    public LocalDate getDate() {
        return date;
    }

    public Staff getStaff() {
        return staff;
    }

    private long getMaxWorkTime() {
        return Duration.between(start, end).toMinutes();
    }

    public Set<Room> getRooms() {
        return rooms;
    }

    private long getWorkTime() {
        long workTime = 0;
        for (Room room : rooms) {
            workTime += room.getCleaningTime();
        }
        return workTime;
    }

    public long getCapacity() {
        return getMaxWorkTime() - getWorkTime();
    }

    private boolean hasCapacity(Room room) {
        return getWorkTime() + room.getCleaningTime() <= getMaxWorkTime();
    }

    public boolean addRoom(Room room) {
        if (hasCapacity(room)) {
            rooms.add(room);
            return true;
        }
        return false;
    }

    public boolean removeRoom(Room room) {
        return rooms.remove(room);
    }

    @Override
    public String toString() {
        return id + "_" + date + "_" + staff + "_" + rooms;
    }
}
