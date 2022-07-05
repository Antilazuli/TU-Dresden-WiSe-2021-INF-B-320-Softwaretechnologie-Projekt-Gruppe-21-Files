package hotel.member.guest;

public class GuestEditForm {

    private final String firstname;
    private final String lastname;
    private final String phoneNumber;
    private final String creditCardNumber;
    private final String newPassword;
    private final String repPassword;

    public GuestEditForm(String firstname, String lastname, String phoneNumber,
            String creditCardNumber, String newPassword, String repPassword) {

        this.firstname = firstname;
        this.lastname = lastname;
        this.phoneNumber = phoneNumber;
        this.creditCardNumber = creditCardNumber;
        this.newPassword = newPassword;
        this.repPassword = repPassword;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getCreditCardNumber() {
        return creditCardNumber;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public String getRepPassword() {
        return repPassword;
    }
}
