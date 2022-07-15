package fr.rushcubeland.commons.cosmetics;

import java.util.Arrays;

public enum ParticlesUnit {

    TARTARE("Tartare", "CRIT", "rcbhub.cosmetics.particles.tartare", "NETHERRACK"),
    ATLANTIDE("Atlantide", "WATER_SPLASH", "rcbhub.cosmetics.particles.atlantide", "WATER_BUCKET"),
    ENFERS("Enfers", "DRIP_LAVA", "rcbhub.cosmetics.particles.enfers", "MAGMA_BLOCK"),
    ELYSEES("Elysées", "CLOUD", "rcbhub.cosmetics.particles.elysees", "SUNFLOWER");

    private final String name;
    private final String particle;
    private final String permission;
    private final String material;

    ParticlesUnit(String name, String particle, String permission, String material) {
        this.name = name;
        this.particle = particle;
        this.permission = permission;
        this.material = material;
    }

    public String getName() {
        return name;
    }

    public String getPermission() {
        return permission;
    }

    public String getParticleName() {
        return particle;
    }

    public String getMaterialName() {
        return material;
    }

    public static ParticlesUnit getByName(String name){
        return Arrays.stream(values()).filter(r -> r.getName().equalsIgnoreCase(name)).findAny().orElse(null);
    }
}
