package fr.rushcubeland.commons;

import java.util.UUID;

public class AStatsDAC implements Cloneable {

    private UUID uuid;
    private int wins;
    private int loses;

    private int nbParties;
    private int nbSortsUsed;

    private int nbJumps;
    private int nbSuccessJumps;
    private int nbFailJumps;

    public AStatsDAC() {
    }

    public AStatsDAC(UUID uuid, int wins, int loses, int nbParties, int nbSortsUsed, int nbJumps, int nbSuccessJumps, int nbFailJumps) {
        this.uuid = uuid;
        this.wins = wins;
        this.loses = loses;
        this.nbParties = nbParties;
        this.nbSortsUsed = nbSortsUsed;
        this.nbJumps = nbJumps;
        this.nbSuccessJumps = nbSuccessJumps;
        this.nbFailJumps = nbFailJumps;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public int getWins() {
        return wins;
    }

    public void setWins(int wins) {
        this.wins = wins;
    }

    public int getLoses() {
        return loses;
    }

    public void setLoses(int loses) {
        this.loses = loses;
    }

    public int getNbParties() {
        return nbParties;
    }

    public void setNbParties(int nbParties) {
        this.nbParties = nbParties;
    }

    public int getNbSortsUsed() {
        return nbSortsUsed;
    }

    public void setNbSortsUsed(int nbSortsUsed) {
        this.nbSortsUsed = nbSortsUsed;
    }

    public int getNbJumps() {
        return nbJumps;
    }

    public void setNbJumps(int nbJumps) {
        this.nbJumps = nbJumps;
    }

    public int getNbSuccessJumps() {
        return nbSuccessJumps;
    }

    public void setNbSuccessJumps(int nbSuccessJumps) {
        this.nbSuccessJumps = nbSuccessJumps;
    }

    public int getNbFailJumps() {
        return nbFailJumps;
    }

    public void setNbFailJumps(int nbFailJumps) {
        this.nbFailJumps = nbFailJumps;
    }

    public AStatsDAC clone(){
        try {

            return (AStatsDAC) super.clone();

        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return null;
    }
}
