package hotel.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import hotel.booking.Booking;
import hotel.booking.BookingRepository;
import hotel.member.Member;
import hotel.member.MemberManagement;
import hotel.member.guest.Guest;

@Component
public class LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    private final MemberManagement memberManagement;
    private final BookingRepository bookings;

    public LoginSuccessHandler(MemberManagement memberManagement, BookingRepository bookings) {

        this.memberManagement = memberManagement;
        this.bookings = bookings;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {

        if (authentication != null) {

            var opt = memberManagement.findByEmail(authentication.getName());

            if (opt.isPresent()) {

                Member member = opt.get();

                if (member instanceof Guest) {
                    bookings.findAllByGuest((Guest) member).forEach(Booking::getState);
                }
            }
        }
        super.onAuthenticationSuccess(request, response, authentication);
    }
}
