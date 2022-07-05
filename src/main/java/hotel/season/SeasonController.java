package hotel.season;

import java.time.LocalDate;
import java.time.Period;
import java.util.Comparator;

import javax.validation.Valid;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@PreAuthorize("hasRole('MANAGER')")
@Controller
public class SeasonController {

    public static final String ADD_SEASON_ROUTE = "manager/addSeason";

    private static final String START = "start";
    private static final String END = "end";

    private final SeasonRepository seasons;

    public SeasonController(SeasonRepository seasons) {

        Assert.notNull(seasons, "SeasonRepository must not be null!");

        this.seasons = seasons;
    }

    @GetMapping("/manager/seasons")
    public String seasons(Model model) {

        model.addAttribute("seasons", seasons.findAll());

        return "manager/seasons";
    }

    private static LocalDate toDate(String date) {

        try {
            return LocalDate.parse(date);
        } catch (Exception e) {
            return null;
        }
    }

    private void validateDates(LocalDate start, LocalDate end, Errors result) {

        if (start == null || end == null) {

            if (start == null) {
                result.rejectValue(START, "0", "Invalides Datum!");
            }
            if (end == null) {
                result.rejectValue(END, "1", "Invalides Datum!");
            }
        } else {

            if (start.isBefore(LocalDate.now())) {
                result.rejectValue(START, "2", "Startdatum muss in der Zukunft liegen!");
            }
            if (!end.isAfter(start)) {
                result.rejectValue(END, "3", "Enddatum muss nach Startdatum liegen!");
            }

            if (Period.between(start, end).toTotalMonths() > 1) {
                result.rejectValue(END, "4", "Zeitraum ist auf einen Monat begrenzt!");
            }

            if (!seasons.findAllActiveFromDate(start).isEmpty()) {

                var minStart = seasons.findAllActiveFromDate(start).stream().min(Comparator.comparing(Season::getStart))
                        .orElseThrow().getStart();
                var maxEnd = seasons.findAllActiveFromDate(start).stream().max(Comparator.comparing(Season::getEnd))
                        .orElseThrow().getEnd();

                if (start.compareTo(maxEnd) <= 0 && minStart.compareTo(end) <= 0) {
                    result.rejectValue(START, "5", "");
                    result.rejectValue(END, "6", "Zeitraum überschneidet sich mit einer anderen Saison!");
                }
            }
        }
    }

    @PostMapping("/addSeason")
    public String addSeason(@Valid SeasonForm form, Errors result) {

        if (result.hasFieldErrors(START) || result.hasFieldErrors(END)) {
            return ADD_SEASON_ROUTE;
        }

        var start = toDate(form.getStart());
        var end = toDate(form.getEnd());

        validateDates(start, end, result);

        if (result.hasErrors()) {
            return ADD_SEASON_ROUTE;
        }

        String name = form.getName();

        var percentage = Integer.parseInt(form.getValue());

        seasons.save(new Season(name, percentage, start, end));

        return "redirect:/manager/seasons";
    }

    @GetMapping("/manager/season/add")
    public String addSeason(Model model, SeasonForm form) {
        return ADD_SEASON_ROUTE;
    }

    @PostMapping("/removeSeason/{season}")
    public String remove(@PathVariable Season season) {

        seasons.delete(season.delete());

        return "redirect:/manager/seasons";
    }
}
