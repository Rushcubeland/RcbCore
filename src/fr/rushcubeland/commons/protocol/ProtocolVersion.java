package fr.rushcubeland.commons.protocol;

import java.util.Arrays;

public enum ProtocolVersion {

    MINECRAFT_1_15("1.15", 573),
    MINECRAFT_1_15_1("1.15.1", 575),
    MINECRAFT_1_15_2("1.15.2", 578),
    MINECRAFT_1_16("1.16", 735),
    MINECRAFT_1_16_1("1.16.1", 736),
    MINECRAFT_1_16_2("1.16.2", 751),
    MINECRAFT_1_16_3("1.16.2", 753),
    MINECRAFT_1_16_5("1.16.5", 754),
    MINECRAFT_1_17("1.17", 755),
    MINECRAFT_1_17_1("1.17.1", 756),
    MINECRAFT_1_18_1("1.18.1", 757),
    MINECRAFT_1_18_2("1.18.1", 758),
    MINECRAFT_1_19("1.19", 759);

    private final String name;
    private final int id;

    ProtocolVersion(String name, int id){
        this.name = name;
        this.id = id;
    }

    public boolean match(ProtocolVersion protocolVersion){
        return protocolVersion == this;
    }

    public static ProtocolVersion valueOf(int version){
        return Arrays.stream(values()).filter(p -> p.getId() == version).findAny().orElse(null);
    }

    @Override
    public String toString() {
        return super.toString();
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }


}
