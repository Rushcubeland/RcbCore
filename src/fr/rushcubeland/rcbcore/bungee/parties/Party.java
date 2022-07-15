package fr.rushcubeland.rcbcore.bungee.parties;

import fr.rushcubeland.commons.AParty;
import fr.rushcubeland.rcbcore.bungee.RcbAPI;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.ArrayList;
import java.util.Optional;

public class Party {

    private ArrayList<ProxiedPlayer> players = new ArrayList<>();
    private final int maxPlayers;

    public Party(int maxPlayers) {
        this.maxPlayers = maxPlayers;
        RcbAPI.getInstance().getParties().add(this);
    }

    public void addPlayer(ProxiedPlayer player){
        if(!this.players.contains(player) && this.players.size() < this.maxPlayers){
            Optional<AParty> aParty = RcbAPI.getInstance().getAccountParty(player);
            if(aParty.isPresent()){
                aParty.get().setParty(this);
                this.players.add(player);
            }
        }
    }

    public void removePlayer(ProxiedPlayer player){
        if(this.players.contains(player)){
            Optional<AParty> aParty = RcbAPI.getInstance().getAccountParty(player);
            if(aParty.isPresent()){
                aParty.get().setParty(null);
                this.players.remove(player);
            }
        }
    }

    public ProxiedPlayer getCaptain(){
        if(!this.players.isEmpty()){
            return this.players.get(0);
        }
        return null;
    }

    public void setCaptain(ProxiedPlayer player){
        if(this.players.contains(player)){
            this.players.remove(player);
            this.players.add(0, player);
        }
    }

    public boolean isInParty(ProxiedPlayer player){
        return this.players.contains(player);
    }

    public ArrayList<ProxiedPlayer> getPlayers() {
        return players;
    }

    public void setPlayers(ArrayList<ProxiedPlayer> players) {
        this.players = players;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public void disbandParty(){
        RcbAPI.getInstance().getParties().remove(this);
    }
}
