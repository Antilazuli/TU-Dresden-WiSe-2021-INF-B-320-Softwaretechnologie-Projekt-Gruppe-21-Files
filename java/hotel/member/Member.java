package hotel.member;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import org.salespointframework.useraccount.Password.EncryptedPassword;
import org.salespointframework.useraccount.Role;
import org.salespointframework.useraccount.UserAccount;

@Entity(name = "Member")
public abstract class Member {

    private @Id @GeneratedValue Long id;
    private String email;
    private Role role;

    @OneToOne
    protected UserAccount userAccount;

    protected Member() {
    }

    public Member(UserAccount userAccount, Role role) {

        userAccount.add(role);

        this.role = role;
        this.userAccount = userAccount;
        this.email = userAccount.getEmail();
    }

    public Long getId() {
        return id;
    }

    public UserAccount getUserAccount() {
        return userAccount;
    }

    public String getFirstname() {
        return userAccount.getFirstname();
    }

    public void setFirstname(String firstname) {
        userAccount.setFirstname(firstname);
    }

    public String getLastname() {
        return userAccount.getLastname();
    }

    public void setLastname(String lastname) {
        userAccount.setLastname(lastname);
    }

    public String getName() {
        return getFirstname() + " " + getLastname();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
        userAccount.setEmail(email);
    }

    public EncryptedPassword getPassword() {
        return userAccount.getPassword();
    }

    public Role getRole() {
        return role;
    }

    @Override
    public String toString() {
        return getName();
    }
}
