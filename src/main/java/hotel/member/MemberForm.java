package hotel.member;

public class MemberForm {

    private final String firstname;
    private final String lastname;
    private final String newPassword;

    public MemberForm(String firstname, String lastname, String newPassword) {

        this.firstname = firstname;
        this.lastname = lastname;
        this.newPassword = newPassword;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getNewPassword() {
        return newPassword;
    }
}
