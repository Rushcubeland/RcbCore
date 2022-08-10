package fr.rushcubeland.commons;

import fr.rushcubeland.commons.rank.RankUnit;

import java.util.UUID;

public class Account implements Cloneable {

    private UUID uuid;
    private RankUnit primaryRank;
    private RankUnit secondaryRank;
    private long primaryRank_end;
    private long secondaryRank_end;
    private boolean state;
    private String server;
    private long coins;

    public Account() {
    }

    public Account(UUID uuid, RankUnit primaryRank, RankUnit secondaryRank, long coins) {
        this(uuid, primaryRank, secondaryRank, -1, -1, coins);
        this.state = false;
    }

    public Account(UUID uuid, RankUnit primaryRank, RankUnit secondaryRank,  long primaryRank_end, long secondaryRank_end, long coins) {
        this.uuid = uuid;
        this.primaryRank = primaryRank;
        this.secondaryRank = secondaryRank;
        this.coins = coins;
        this.primaryRank_end = primaryRank_end;
        this.secondaryRank_end = secondaryRank_end;
        this.state = false;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public long getRank_end() {
        if(primaryRank == null){
            return secondaryRank_end;
        }
        return primaryRank_end;
    }

    public void setRank(RankUnit rank) {
        secondaryRank = rank;
        secondaryRank_end = -1;
    }

    public void setRank(RankUnit rank, long seconds) {
        if(seconds <= 0){
            setRank(rank);
        }
        else
        {
            secondaryRank = rank;
            this.secondaryRank_end = seconds*1000 + System.currentTimeMillis();
        }
    }

    public RankUnit getPrimaryRank() {
        return primaryRank;
    }

    public RankUnit getSecondaryRank() {
        return secondaryRank;
    }

    public RankUnit getRank(){
        if(primaryRank == null){
            return secondaryRank;
        }
        return primaryRank;
    }

    public long getPrimaryRank_end() {
        return primaryRank_end;
    }

    public long getSecondaryRank_end() {
        return secondaryRank_end;
    }

    public long getCoins() {
        return coins;
    }

    public void setCoins(long coins) {
        this.coins = coins;
    }

    public boolean primaryRankIsTemporary(){
        return primaryRank_end != -1;
    }

    public boolean secondaryRankIsTemporary(){
        return secondaryRank_end != -1;
    }

    public boolean getState() {
        return state;
    }

    public String getServer() {
        return server;
    }

    public boolean isState(boolean state){
        if(state == this.state){
            return true;
        }
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public boolean RankIsValid(){
        if(primaryRank != null){
            if(!primaryRankIsValid()){
                primaryRank = null;
                primaryRank_end = -1;
                return false;

            }
        }
        else if(!secondaryRankIsValid()){
            secondaryRank = RankUnit.JOUEUR;
            secondaryRank_end = -1;
            return false;
        }
        return true;
    }

    public boolean primaryRankIsValid(){
        return primaryRank_end != -1 && primaryRank_end > System.currentTimeMillis();
    }

    public boolean secondaryRankIsValid(){
        return secondaryRank_end != -1 && secondaryRank_end > System.currentTimeMillis();
    }

    public Account clone(){
        try {

            return (Account) super.clone();

        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return null;
    }

}