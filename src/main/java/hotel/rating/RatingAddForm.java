package hotel.rating;

import javax.validation.constraints.Pattern;

public class RatingAddForm {

    @Pattern(regexp = "[1-5]", message = "Zahl zwischen 1 und 5 erforderlich!")
    private String star;
    private String comment;

    public RatingAddForm(String star, String comment) {

        this.star = star;
        this.comment = comment;
    }

    public String getStar() {

        return star;
    }

    public String getComment() {

        return comment;
    }

}
