package hotel.season;

import java.time.LocalDate;

import org.salespointframework.core.SalespointIdentifier;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.util.Streamable;

public interface SeasonRepository extends CrudRepository<Season, SalespointIdentifier> {

    @Override
    Streamable<Season> findAll();

    @Query("select s from Season s where s.start >= :date or s.end >= :date")
    Streamable<Season> findAllActiveFromDate(LocalDate date);
}
