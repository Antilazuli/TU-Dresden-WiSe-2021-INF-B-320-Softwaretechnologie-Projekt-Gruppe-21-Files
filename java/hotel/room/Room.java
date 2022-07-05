package hotel.room;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import javax.money.MonetaryAmount;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;

import org.javamoney.moneta.Money;
import org.salespointframework.catalog.Product;
import org.salespointframework.core.Currencies;

@Entity
public class Room extends Product {

    static class SortByType implements Comparator<Room> {

        @Override
        public int compare(Room o1, Room o2) {
            int result = o1.getType().compareTo(o2.getType());
            return result != 0 ? result : o1.compareTo(o2);
        }
    }

    static class SortByPrice implements Comparator<Room> {

        @Override
        public int compare(Room o1, Room o2) {
            return o1.getPrice().compareTo(o2.getPrice());
        }
    }

    static class SortByEquipment implements Comparator<Room> {

        @Override
        public int compare(Room o1, Room o2) {
            int result = o1.getEquipments().size() - o2.getEquipments().size();
            return result != 0 ? result : o1.compareTo(o2);
        }
    }

    public enum SortOption {
        TYPE {
            @Override
            public Comparator<Room> getComparator() {
                return new SortByType();
            }
        },
        PRICE {
            @Override
            public Comparator<Room> getComparator() {
                return new SortByPrice();
            }
        },
        EQUIPMENT {
            @Override
            public Comparator<Room> getComparator() {
                return new SortByEquipment();
            }
        };

        public abstract Comparator<Room> getComparator();
    }

    public enum Type {
        SINGLE {
            @Override
            public String toString() {
                return "Einzelzimmer";
            }

            @Override
            public int getCleaningTime() {
                return 30;
            }
        },
        DOUBLE {
            @Override
            public String toString() {
                return "Doppelzimmer";
            }

            @Override
            public int getCleaningTime() {
                return 30;
            }
        },
        SUITE {
            @Override
            public String toString() {
                return "Suite";
            }

            @Override
            public int getCleaningTime() {
                return 60;
            }
        };

        public abstract int getCleaningTime();
    }

    public enum Equipment {
        TELEVISION {
            @Override
            public String toString() {
                return "Fernseher";
            }
        },
        WATER_HEATER {
            @Override
            public String toString() {
                return "Wasserkocher";
            }
        },
        AIR_CONDITIONER {
            @Override
            public String toString() {
                return "Klimaanlage";
            }
        },
        HAIRDRYER {
            @Override
            public String toString() {
                return "Föhn";
            }
        },
        DESK {
            @Override
            public String toString() {
                return "Tisch";
            }
        },
        COUCH {
            @Override
            public String toString() {
                return "Sofa";
            }
        },
        ARMCHAIR {
            @Override
            public String toString() {
                return "Sessel";
            }
        },
        SAFE {
            @Override
            public String toString() {
                return "Tresor";
            }
        },
        BATHTUB {
            @Override
            public String toString() {
                return "Badewanne";
            }
        },
        SHOWER {
            @Override
            public String toString() {
                return "Dusche";
            }
        };
    }

    private Type type;

    @ElementCollection
    @Column(name = "equipments", length = 1000)
    private Set<Equipment> equipments;

    @SuppressWarnings("deprecation")
    protected Room() {
    }

    public Room(String name, MonetaryAmount price, Type type, Set<Equipment> equipments) {

        super(name, price);
        this.type = type;
        this.equipments = equipments;
    }

    public static Room getRandom(Random r, int i) {

        String name = String.format("Zimmer %03d", i);
        MonetaryAmount price = Money.of(r.nextInt(200), Currencies.EURO);
        Type type = Type.values()[r.nextInt(Type.values().length)];
        Equipment[] e = Equipment.values();
        int count = r.nextInt(e.length);
        Set<Equipment> equipments = new HashSet<>();

        while (equipments.size() < count) {
            equipments.add(Equipment.values()[r.nextInt(e.length)]);
        }

        return new Room(name, price, type, equipments);
    }

    public static SortByType sortByType() {
        return new SortByType();
    }

    public static SortByEquipment sortByEquipment() {
        return new SortByEquipment();
    }

    public Type getType() {
        return type;
    }

    public int getCleaningTime() {
        return type.getCleaningTime();
    }

    public Set<Equipment> getEquipments() {
        return equipments;
    }

    public void setEquipments(Set<Equipment> equipments) {
        this.equipments = equipments;
    }

    public void addEquipments(Set<Equipment> equipments) {
        for (Equipment equipment : equipments) {
            this.equipments.add(equipment);
        }
    }

    public void removeEquipments(Set<Equipment> equipments) {
        for (Equipment equipment : equipments) {
            this.equipments.remove(equipment);
        }
    }

    public boolean containsEquipment(Set<Equipment> equipments) {

        if (this.equipments.containsAll(equipments)) {

            return true;
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return getName();
    }
}
