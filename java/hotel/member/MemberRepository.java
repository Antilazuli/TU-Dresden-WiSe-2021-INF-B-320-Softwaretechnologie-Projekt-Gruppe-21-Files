package hotel.member;

import java.util.Optional;

import org.salespointframework.useraccount.UserAccount;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.util.Streamable;

import hotel.member.guest.Guest;
import hotel.member.staff.Staff;

public interface MemberRepository extends CrudRepository<Member, Long> {

    @Query("select g from Guest g")
    Streamable<Guest> findAllGuests();

    @Query("select s from Staff s")
    Streamable<Staff> findAllStaffs();

    Optional<Member> findById(Long id);

    Optional<Member> findByEmail(String email);

    Member findByUserAccount(UserAccount userAccount);
}
