package hotel.rating;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.salespointframework.useraccount.Role;
import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.web.LoggedIn;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class RatingController {

    private final RatingRepository ratings;

    public RatingController(RatingRepository ratings) {

        Assert.notNull(ratings, "RatingRepository must not be null!");

        this.ratings = ratings;
    }

    @GetMapping("/ratings")
    public String rating(Model model, @LoggedIn Optional<UserAccount> userAccount) {

        model.addAttribute("ratings",
                ratings.findAll().stream().sorted(Comparator.comparing(Rating::getDateTime).reversed())
                        .collect(Collectors.toList()));

        // Bewertung erstellen
        model.addAttribute("authorize", userAccount.isPresent() && userAccount.get().hasRole(Role.of("RATING")));

        // Durchschnitt Bewertungen
        var avg = ratings.findAll().stream().mapToInt(Rating::getRating).average();
        if (avg.isPresent()) {

            Double average = Math.round(avg.getAsDouble() * 10) / 10.0;
            model.addAttribute("average", average);
        }

        return "ratings";
    }

    @PreAuthorize("hasRole('RATING')")
    @GetMapping("/add-rating")
    public String addRating(RatingAddForm form) {

        return "guest/addRating";
    }

    @PreAuthorize("hasRole('RATING')")
    @PostMapping("/add-rating")
    public String addRatingForm(@Valid RatingAddForm form, Errors errors) {

        // Validierung für Sterneingabe
        if (errors.hasErrors()) {

            return "guest/addRating";
        }

        Rating rating = new Rating(Integer.parseInt(form.getStar()), form.getComment(), LocalDateTime.now());
        ratings.save(rating);

        return "redirect:/ratings";
    }
}
