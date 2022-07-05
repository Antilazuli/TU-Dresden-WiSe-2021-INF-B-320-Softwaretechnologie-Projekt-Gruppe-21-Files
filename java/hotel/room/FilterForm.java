package hotel.room;

import java.util.List;

public class FilterForm {

    private final String start;
    private final String end;

    private final List<String> types;
    private final List<String> equipments;

    public FilterForm(String startString, String endString, List<String> types, List<String> equipments) {

        this.start = startString;
        this.end = endString;

        this.types = types != null ? types : List.of();
        this.equipments = equipments != null ? equipments : List.of();
    }

    public String getStart() {
        return start;
    }

    public String getEnd() {
        return end;
    }

    public List<String> getEquipments() {
        return equipments;
    }

    public List<String> getTypes() {
        return types;
    }
}
