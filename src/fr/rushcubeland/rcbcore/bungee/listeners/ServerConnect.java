package fr.rushcubeland.rcbcore.bungee.listeners;

import fr.rushcubeland.commons.Account;
import fr.rushcubeland.rcbcore.bungee.RcbAPI;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class ServerConnect implements Listener {

    @EventHandler
    public void onConnect(ServerConnectEvent event){
        ProxiedPlayer player = event.getPlayer();
        Account account = RcbAPI.getInstance().getAccount(player);
        account.setServer(event.getRequest().getTarget().getName());
        RcbAPI.getInstance().sendAccountToRedis(account);
    }
}
