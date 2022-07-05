package hotel.member.manager;

import javax.persistence.Entity;

import org.salespointframework.useraccount.UserAccount;

import hotel.member.Member;
import hotel.member.MemberManagement;

@Entity(name = "Manager")
public class Manager extends Member {

    @SuppressWarnings("unused")
    private Manager() {
    }

    public Manager(UserAccount userAccount) {
        super(userAccount, MemberManagement.MANAGER_ROLE);
    }
}
