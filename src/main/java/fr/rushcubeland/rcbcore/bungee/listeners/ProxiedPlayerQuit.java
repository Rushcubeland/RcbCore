package fr.rushcubeland.rcbcore.bungee.listeners;

import fr.rushcubeland.commons.AParty;
import fr.rushcubeland.commons.AStats;
import fr.rushcubeland.commons.Account;
import fr.rushcubeland.rcbcore.bungee.RcbAPI;
import fr.rushcubeland.rcbcore.bungee.friends.Friend;
import fr.rushcubeland.rcbcore.bungee.parties.Parties;
import fr.rushcubeland.rcbcore.bungee.parties.Party;
import fr.rushcubeland.rcbcore.bungee.queue.Queue;
import fr.rushcubeland.rcbcore.bungee.queue.QueueUnit;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.Date;
import java.util.Optional;

public class ProxiedPlayerQuit implements Listener {

    @EventHandler
    public void onQuit(PlayerDisconnectEvent e){
        ProxiedPlayer player = e.getPlayer();
        Account account = RcbAPI.getInstance().getAccount(player);
        AStats aStats = RcbAPI.getInstance().getAccountStats(player);
        aStats.setLastConnection(new Date(System.currentTimeMillis()));
        account.setState(false);
        RcbAPI.getInstance().sendAccountToRedis(account);
        RcbAPI.getInstance().sendAccountStatsToRedis(aStats);
        removeMpData(player);
        removePartyData(player);
        removeQueueData(player);
        Friend.quitNotifFriends(player);
        Friend.onQuit(player);

    }

    private void removeQueueData(ProxiedPlayer player){
        if(Queue.proxiedPlayerIsInAQueue(player)){
            QueueUnit queueUnit = Queue.getCurrentQueue(player);
            if(queueUnit != null){
                queueUnit.getPlayers().remove(player);
            }
        }
    }

    private void removeMpData(ProxiedPlayer player){
        ProxiedPlayer target = RcbAPI.getInstance().getMpData().get(player);
        RcbAPI.getInstance().getMpData().remove(target);
        RcbAPI.getInstance().getMpData().remove(player);
    }

    private void removePartyData(ProxiedPlayer player){
        Parties.getDatarequest().remove(player);
        if(RcbAPI.getInstance() == null){
            return;
        }
        Optional<AParty> aParty = RcbAPI.getInstance().getAccountParty(player);
        if(aParty.isPresent() && aParty.get().isInParty()){
            Party party = aParty.get().getParty();
            party.removePlayer(player);
            if(!party.getPlayers().isEmpty()){
                party.setCaptain(party.getPlayers().get(0));
            }
            aParty.get().setParty(null);
        }
    }
}
