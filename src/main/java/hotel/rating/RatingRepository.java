package hotel.rating;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.util.Streamable;

public interface RatingRepository extends CrudRepository<Rating, Long> {

    Streamable<Rating> findAll();

}
