package hotel.room;

import java.util.List;

public class RoomAddForm {

    private final String name;
    private final String price;
    private final String type;

    private final List<String> equipments;

    public RoomAddForm(String name, String price, String type, List<String> equipments) {

        this.name = name;
        this.price = price;
        this.type = type;

        this.equipments = equipments != null ? equipments : List.of();
    }

    public String getName() {
        return name;
    }

    public String getPrice() {
        return price;
    }

    public String getType() {
        return type;
    }

    public List<String> getEquipments() {
        return equipments;
    }
}
