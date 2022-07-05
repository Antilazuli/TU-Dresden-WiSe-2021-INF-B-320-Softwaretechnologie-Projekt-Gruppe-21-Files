package hotel.season;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class SeasonForm {

    @NotBlank(message = "Name erforderlich!")
    private final String name;
    @Pattern(regexp = "[1-9]+[0-9]*", message = "Positive ganze Zahl erforderlich!")
    @Size(max = 3, message = "Zahl ist zu groß!")
    private final String value;
    @NotBlank(message = "Startdatum erforderlich!")
    private final String start;
    @NotBlank(message = "Enddatum erforderlich!")
    private final String end;

    public SeasonForm(String name, String value, String start, String end) {

        this.name = name;
        this.value = value;
        this.start = start;
        this.end = end;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    public String getStart() {
        return start;
    }

    public String getEnd() {
        return end;
    }
}
