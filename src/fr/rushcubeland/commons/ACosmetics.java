package fr.rushcubeland.commons;

import fr.rushcubeland.commons.cosmetics.ParticlesUnit;
import fr.rushcubeland.commons.cosmetics.PetsUnit;

import java.util.UUID;

public class ACosmetics implements Cloneable {

    private UUID uuid;
    private ParticlesUnit currentParticles;
    private PetsUnit currentPet;

    public ACosmetics() {
    }

    public ACosmetics(UUID uuid, ParticlesUnit currentParticles, PetsUnit currentPet) {
        this.uuid = uuid;
        this.currentParticles = currentParticles;
        this.currentPet = currentPet;
    }

    public ParticlesUnit getCurrentParticles() {
        return currentParticles;
    }

    public void setCurrentParticles(ParticlesUnit currentParticles) {
        this.currentParticles = currentParticles;
    }

    public PetsUnit getCurrentPet() {
        return currentPet;
    }

    public void setCurrentPet(PetsUnit currentPet) {
        this.currentPet = currentPet;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public ACosmetics clone(){
        try {

            return (ACosmetics) super.clone();

        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return null;
    }
}