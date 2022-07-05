package hotel.roster;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.util.Streamable;

import hotel.member.staff.Staff;

public interface WorkingDayRepository extends CrudRepository<WorkingDay, Long> {

    public Streamable<WorkingDay> findByDate(LocalDate date);

    public Streamable<WorkingDay> findByStaff(Staff staff);

    public Optional<WorkingDay> findByStaffAndDate(Staff staff, LocalDate date);

    @Query(("select wd from WorkingDay wd where wd.staff.id = :staffId" +
            " and wd.date between :start and :end order by date asc"))
    public Streamable<WorkingDay> findByIdBetweenDates(long staffId, LocalDate start, LocalDate end);
}
