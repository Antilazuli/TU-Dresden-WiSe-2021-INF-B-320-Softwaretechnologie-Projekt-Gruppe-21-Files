package hotel.roster;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.NoSuchElementException;
import java.util.Set;

import org.springframework.stereotype.Service;

import hotel.member.MemberRepository;
import hotel.member.staff.Staff;
import hotel.room.Room;
import hotel.roster.vacation.VacationDay;
import hotel.roster.vacation.VacationRequest;

@Service
public class RosterManagement {

    private final WorkingDayRepository workingDays;
    private final MemberRepository members;

    public RosterManagement(WorkingDayRepository workingDays, MemberRepository members) {

        this.workingDays = workingDays;
        this.members = members;
    }

    public void createWorkingDays(LocalDate[] dateSpan) {
        for (LocalDate date : dateSpan) {
            // Arbeitstag für jeden Staff anlegen
            members.findAllStaffs().forEach(staff -> {
                if (workingDays.findByStaffAndDate(staff, date).isEmpty()) {
                    WorkingDay workingDay = new WorkingDay(date, staff);
                    workingDays.save(workingDay);
                }
            });
        }
    }

    public void assignRooms(LocalDate[] dateSpan, Set<Room> rooms) {
        createWorkingDays(dateSpan);
        for (LocalDate date : dateSpan) {
            for (Room room : rooms) {

                WorkingDay workingDay = workingDays.findByDate(date).stream()
                        .max(Comparator.comparing(WorkingDay::getCapacity)).orElseThrow(NoSuchElementException::new);

                if (workingDay.addRoom(room)) {
                    workingDays.save(workingDay);
                } else {
                    System.out.println("Konnte nicht zugeordnet werden: " + room);
                }
            }
        }
    }

    public void unassignRooms(LocalDate[] dateSpan, Set<Room> rooms) {
        for (LocalDate date : dateSpan) {
            for (Room room : rooms) {
                for (WorkingDay workingDay : workingDays.findByDate(date)) {
                    workingDay.removeRoom(room);
                    workingDays.save(workingDay);
                }
            }
        }
    }

    public void addVacation(Staff staff, LocalDate start, LocalDate end) {

        for (WorkingDay workingDay : workingDays.findByIdBetweenDates(staff.getId(), start, end)) {

            VacationDay vacationDay = new VacationDay(workingDay);

            LocalDate[] dates = { vacationDay.getDate() };

            Set<Room> rooms = vacationDay.clearRooms();

            workingDays.delete(workingDay);
            workingDays.save(vacationDay);

            assignRooms(dates, rooms);
        }
    }

    public void acceptVacationRequest(VacationRequest vacationRequest) {
        Staff staff = vacationRequest.getStaff();
        LocalDate start = vacationRequest.getStart();
        LocalDate end = vacationRequest.getEnd();

        addVacation(staff, start, end);
    }

    public void deleteAllWorkingDays(Staff staff) {

        LocalDate start = LocalDate.now();
        LocalDate end = workingDays.findByStaff(staff).stream().max(Comparator.comparing(WorkingDay::getDate))
                .orElseThrow(NoSuchElementException::new).getDate();

        addVacation(staff, start, end);

        workingDays.deleteAll(workingDays.findByStaff(staff));
    }
}
