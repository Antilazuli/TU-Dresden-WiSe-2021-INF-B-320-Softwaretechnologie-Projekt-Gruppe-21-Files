package hotel.rating;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Comparator;
import java.util.Optional;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.salespointframework.useraccount.Role;
import org.salespointframework.useraccount.UserAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;

import hotel.AbstractIntegrationTests;

public class RatingControllerIntegrationTests extends AbstractIntegrationTests {

    @Autowired
    RatingController controller;

    @Autowired
    RatingRepository ratings;

    @Test
    @WithMockUser(roles = "RATING")
    void addRating() {

        String returnedView = controller.addRating(mock(RatingAddForm.class));

        assertEquals("guest/addRating", returnedView);
    }

    @Test
    @WithMockUser(roles = "RATING")
    void addRatingFormWithErrors() {

        Errors errors = mock(Errors.class);
        when(errors.hasErrors()).thenReturn(true);

        String returnedView = controller.addRatingForm(new RatingAddForm("9", ""), errors);

        assertEquals("guest/addRating", returnedView);
    }

    @Test
    @WithMockUser(roles = "RATING")
    void addRatingFormWithoutErrors() {

        String returnedView = controller.addRatingForm(new RatingAddForm("4", "Hallo"), mock(Errors.class));

        assertEquals("redirect:/ratings", returnedView);

        Rating rating = ratings.findAll().toList().get(ratings.findAll().toList().size() - 1);
        assertNotNull(rating);
        assertEquals(4, rating.getRating());
        assertEquals("Hallo", rating.getText());
    }

    @Test
    void rating() {

        Model model = new ExtendedModelMap();

        String returnedView = controller.rating(model, Optional.empty());

        assertEquals("ratings", returnedView);

        Object ratingsObject = model.asMap().get("ratings");
        assertNotNull(ratingsObject);
        assertEquals(ratings.findAll().stream().sorted(Comparator.comparing(Rating::getDateTime).reversed())
                .collect(Collectors.toList()), ratingsObject);
    }

    @Test
    void ratingAuthorizeFalse() {

        Model model = new ExtendedModelMap();

        String returnedView = controller.rating(model, Optional.empty());

        assertEquals("ratings", returnedView);

        Object authorizeObject = model.asMap().get("authorize");
        assertNotNull(authorizeObject);
        assertEquals(false, authorizeObject);
    }

    @Test
    void ratingAuthorizeFalseToo() {

        Model model = new ExtendedModelMap();
        UserAccount userAccount = mock(UserAccount.class);
        when(userAccount.hasRole(Role.of("RATING"))).thenReturn(false);

        String returnedView = controller.rating(model, Optional.of(userAccount));

        assertEquals("ratings", returnedView);

        Object authorizeObject = model.asMap().get("authorize");
        assertNotNull(authorizeObject);
        assertEquals(false, authorizeObject);
    }

    @Test
    void ratingAuthorizeTrue() {

        Model model = new ExtendedModelMap();
        UserAccount userAccount = mock(UserAccount.class);
        when(userAccount.hasRole(Role.of("RATING"))).thenReturn(true);

        String returnedView = controller.rating(model, Optional.of(userAccount));

        assertEquals("ratings", returnedView);

        Object authorizeObject = model.asMap().get("authorize");
        assertNotNull(authorizeObject);
        assertEquals(true, authorizeObject);
    }

    @Test
    void ratingAverage() {

        Model model = new ExtendedModelMap();

        String returnedView = controller.rating(model, Optional.empty());

        assertEquals("ratings", returnedView);

        Object averageObject = model.asMap().get("average");
        assertNotNull(averageObject);
        assertEquals(4.5, averageObject);
    }

    @Test
    void ratingAverageNull() {

        Model model = new ExtendedModelMap();

        ratings.deleteAll();

        String returnedView = controller.rating(model, Optional.empty());

        assertEquals("ratings", returnedView);
    }

}