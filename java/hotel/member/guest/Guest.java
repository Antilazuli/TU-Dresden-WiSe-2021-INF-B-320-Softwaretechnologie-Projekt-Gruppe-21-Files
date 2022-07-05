package hotel.member.guest;

import javax.persistence.Entity;

import org.salespointframework.useraccount.UserAccount;

import hotel.member.Member;
import hotel.member.MemberManagement;

@Entity(name = "Guest")
public class Guest extends Member {

    private String phoneNumber;
    private String creditCardNumber;

    @SuppressWarnings("unused")
    private Guest() {
    }

    public Guest(UserAccount userAccount) {
        this(userAccount, null, null);
    }

    public Guest(UserAccount userAccount, String phoneNumber, String creditCardNumber) {
        super(userAccount, MemberManagement.GUEST_ROLE);

        this.phoneNumber = phoneNumber;
        this.creditCardNumber = creditCardNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String number) {
        phoneNumber = number;
    }

    public String getCreditCardNumber() {
        return creditCardNumber;
    }

    public void setCreditCardNumber(String number) {
        creditCardNumber = number;
    }

    public void checkIn() {
        super.userAccount.add(MemberManagement.SERVICE_ROLE);
        super.userAccount.add(MemberManagement.RATING_ROLE);
    }

    public void checkOut() {
        super.userAccount.remove(MemberManagement.SERVICE_ROLE);
    }

    public boolean isCheckedIn() {
        return getUserAccount().hasRole(MemberManagement.SERVICE_ROLE);
    }
}
