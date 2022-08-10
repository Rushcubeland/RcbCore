package fr.rushcubeland.rcbcore.bungee.listeners;

import fr.rushcubeland.rcbcore.bungee.network.Network;
import fr.rushcubeland.rcbcore.bungee.network.ServerGroup;
import fr.rushcubeland.rcbcore.bungee.network.ServerUnit;
import net.md_5.bungee.api.AbstractReconnectHandler;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ServerKickEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class ServerKick implements Listener {

    @EventHandler
    public void onServerKickEvent(ServerKickEvent event) {

        ProxiedPlayer player = event.getPlayer();

        ServerInfo kickedFrom = null;

        if (player.getServer() != null) {
            kickedFrom = player.getServer().getInfo();
        } else if (ProxyServer.getInstance().getReconnectHandler() != null) {
            kickedFrom = ProxyServer.getInstance().getReconnectHandler().getServer(player);
        } else {
            kickedFrom = AbstractReconnectHandler.getForcedHost(player.getPendingConnection());
            if (kickedFrom == null) {
                kickedFrom = ProxyServer.getInstance().getServerInfo(player.getPendingConnection().getListener().getServerPriority().get(0));
            }
        }

        ServerUnit lobbyUnit = Network.getBestServer(event.getPlayer(), ServerGroup.Lobby);
        if(lobbyUnit != null){
            ServerInfo kickTo = lobbyUnit.getServerInfo();
            if (kickedFrom != null && kickedFrom.equals(kickTo)) {
                return;
            }
            event.setCancelled(true);
            event.setCancelServer(kickTo);
            player.sendMessage(new TextComponent("§cVous avez été reconnecté au §6Lobby §c!"));
        }
    }
}
