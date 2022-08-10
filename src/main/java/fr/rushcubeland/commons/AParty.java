package fr.rushcubeland.commons;

import fr.rushcubeland.rcbcore.bungee.RcbAPI;
import fr.rushcubeland.rcbcore.bungee.parties.Party;

import java.util.UUID;

public class AParty {

    private Party party;

    private final UUID uuid;

    public AParty(UUID uuid) {
        this.uuid = uuid;
        if(!RcbAPI.getInstance().getAPartyList().contains(this)){
            RcbAPI.getInstance().getAPartyList().add(this);
        }
    }

    public boolean isInParty(){
        return party != null;
    }

    public Party getParty() {
        return party;
    }

    public void setParty(Party party) {
        this.party = party;
    }

    public UUID getUuid() {
        return uuid;
    }
}
