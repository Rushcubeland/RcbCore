package fr.rushcubeland.commons.cosmetics;

import java.util.Arrays;

public enum PetsUnit {

    PIG("Cerbère", "WOLF", 5000);

    private final String name;
    private final String type;
    private final long price;

    PetsUnit(String name, String type, long price) {
        this.name = name;
        this.type = type;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public long getPrice() {
        return price;
    }

    public static PetsUnit getByName(String name){
        return Arrays.stream(values()).filter(r -> r.getName().equalsIgnoreCase(name)).findAny().orElse(null);
    }
}
