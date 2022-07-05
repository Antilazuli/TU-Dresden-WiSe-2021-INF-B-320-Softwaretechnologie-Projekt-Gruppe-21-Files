package hotel.member;

import javax.validation.constraints.NotBlank;

public class RegistrationForm {

    @NotBlank(message = "Firstname is mandatory!")
    private final String firstname;
    @NotBlank(message = "Lastname is mandatory!")
    private final String lastname;
    @NotBlank(message = "Email is mandatory!")
    private final String email;
    @NotBlank(message = "Password is mandatory!")
    private final String password;

    private final String phoneNumber;
    private final String creditCardNumber;

    public RegistrationForm(String firstname, String lastname, String email, String password, String phoneNumber,
            String creditCardNumber) {

        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.creditCardNumber = creditCardNumber;

    }

    public static RegistrationForm of(String firstname, String lastname, String email, String password) {
        return new RegistrationForm(firstname, lastname, email, password, null, null);
    }

    public String getName() {
        return firstname + " " + lastname;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getCreditCardNumber() {
        return creditCardNumber;
    }
}
