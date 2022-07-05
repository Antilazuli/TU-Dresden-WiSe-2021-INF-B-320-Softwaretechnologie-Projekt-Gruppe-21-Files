package hotel.member.staff;

import javax.persistence.Entity;

import org.salespointframework.useraccount.UserAccount;

import hotel.member.Member;
import hotel.member.MemberManagement;

@Entity(name = "Staff")
public class Staff extends Member {

    @SuppressWarnings("unused")
    private Staff() {
    }

    public Staff(UserAccount userAccount) {
        super(userAccount, MemberManagement.STAFF_ROLE);
    }
}
