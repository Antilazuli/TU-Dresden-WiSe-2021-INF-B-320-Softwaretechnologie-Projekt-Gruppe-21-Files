package hotel.roster.vacation;

import java.time.LocalDate;

import org.salespointframework.core.SalespointIdentifier;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.util.Streamable;

import hotel.member.staff.Staff;

public interface VacationRepository extends CrudRepository<VacationRequest, SalespointIdentifier> {

    Streamable<VacationRequest> findAllByStaff(Staff staff);

    @Query("select v from VacationRequest v where v.staff = :staff and v.start >= :date or v.end >= :date")
    Streamable<VacationRequest> findAllActiveFromDate(Staff staff, LocalDate date);
}
