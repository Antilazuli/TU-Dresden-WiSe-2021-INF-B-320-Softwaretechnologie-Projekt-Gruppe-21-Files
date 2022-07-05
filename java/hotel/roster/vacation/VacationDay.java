package hotel.roster.vacation;

import java.util.Set;

import javax.persistence.Entity;

import hotel.room.Room;
import hotel.roster.WorkingDay;

@Entity(name = "VacationDay")
public class VacationDay extends WorkingDay {

    @SuppressWarnings("unused")
    private VacationDay() {
    }

    public VacationDay(WorkingDay workingDay) {
        super(workingDay.getDate(), workingDay.getStaff());
        super.rooms = workingDay.getRooms();
    }

    public Set<Room> clearRooms() {
        Set<Room> rooms = Set.copyOf(super.rooms);
        super.rooms.clear();
        return rooms;
    }

    @Override
    public boolean addRoom(Room room) {
        return false;
    }
}
