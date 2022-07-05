package hotel.roster.vacation;

import javax.validation.constraints.NotBlank;

public class VacationForm {

    @NotBlank(message = "Startdatum erforderlich!")
    private final String start;
    @NotBlank(message = "Enddatum erforlderlich!")
    private final String end;

    private final String comment;

    public VacationForm(String start, String end, String comment) {

        this.start = start;
        this.end = end;
        this.comment = comment;
    }

    public String getStart() {
        return start;
    }

    public String getEnd() {
        return end;
    }

    public String getComment() {
        return comment;
    }
}
