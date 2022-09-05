package fr.rushcubeland.commons;

import fr.rushcubeland.commons.rank.RankUnit;

import java.util.UUID;

public class Account implements Cloneable {

    private UUID uuid;
    private RankUnit specialRank;
    private RankUnit secondaryRank;
    private long specialRank_end;
    private long secondaryRank_end;
    private boolean state;
    private String server;
    private long coins;

    public Account() {
    }

    public Account(UUID uuid, RankUnit specialRank, RankUnit secondaryRank, long coins) {
        this(uuid, specialRank, secondaryRank, -1, -1, coins);
        this.state = false;
    }

    public Account(UUID uuid, RankUnit specialRank, RankUnit secondaryRank,  long specialRank_end, long secondaryRank_end, long coins) {
        this.uuid = uuid;
        this.specialRank = specialRank;
        this.secondaryRank = secondaryRank;
        this.coins = coins;
        this.specialRank_end = specialRank_end;
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
        if(specialRank == null){
            return secondaryRank_end;
        }
        return specialRank_end;
    }

    public void setRank(RankUnit rank) {
        if(rank == null){
            throw new IllegalArgumentException("Rank can't be null");
        }
        if(rank.isSpecial()){
            setSpecialRank(rank);
            return;
        }
        secondaryRank = rank;
        secondaryRank_end = -1;
    }

    public void setRank(RankUnit rank, long seconds) {
        if(rank == null){
            throw new IllegalArgumentException("Rank can't be null");
        }
        if(rank.isSpecial()){
            setSpecialRank(rank, seconds);
            return;
        }
        if(seconds <= 0){
            setRank(rank);
        }
        else
        {
            secondaryRank = rank;
            this.secondaryRank_end = seconds*1000 + System.currentTimeMillis();
        }
    }

    public void removeSpecialRank(){
        this.specialRank = null;
        this.specialRank_end = -1;
    }

    private void setSpecialRank(RankUnit rank) {
        if(!rank.isSpecial()){
            throw new IllegalArgumentException("The rank must be special to be primary");
        }
        specialRank = rank;
        specialRank_end = -1;
    }

    private void setSpecialRank(RankUnit rank, long seconds) {
        if(!rank.isSpecial()){
            throw new IllegalArgumentException("The rank must be special to be primary");
        }
        if(seconds <= 0){
            setSpecialRank(rank);
        }
        else
        {
            specialRank = rank;
            this.specialRank_end = seconds*1000 + System.currentTimeMillis();
        }
    }


    public RankUnit getSpecialRank() {
        return specialRank;
    }

    public RankUnit getSecondaryRank() {
        return secondaryRank;
    }

    public RankUnit getRank(){
        if(specialRank == null){
            return secondaryRank;
        }
        return specialRank;
    }

    public long getSpecialRank_end() {
        return specialRank_end;
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

    public boolean specialRankIsTemporary(){
        return specialRank_end != -1;
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
        if(specialRank != null){
            if(!specialRankIsValid()){
                specialRank = null;
                specialRank_end = -1;
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

    public boolean specialRankIsValid(){
        return specialRank_end != -1 && specialRank_end > System.currentTimeMillis();
    }

    public boolean secondaryRankIsValid(){
        return secondaryRank_end != -1 && secondaryRank_end > System.currentTimeMillis();
    }

    public void removeCoins(long coins){
        if(this.coins - coins >= 0L){
            this.coins -= coins;
        }
        else
        {
            throw new IllegalArgumentException("Coins can't be negative");
        }
    }

    public void addCoins(long coins){
        if(this.coins + coins >= 0L){
            this.coins += coins;
        }
        else
        {
            throw new IllegalArgumentException("Coins can't be negative");
        }
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